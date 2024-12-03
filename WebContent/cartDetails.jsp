<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.shashi.service.impl.*, com.shashi.service.*,com.shashi.beans.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Cart Details</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body style="background-color: #E6F9E6;">

	<%
	// Check user credentials
	String userName = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");

	if (userName == null || password == null) {
		response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
	}

	// Handle cart updates
	String addS = request.getParameter("add");
	if (addS != null) {
		int add = Integer.parseInt(addS);
		String uid = request.getParameter("uid");
		String pid = request.getParameter("pid");
		int avail = Integer.parseInt(request.getParameter("avail"));
		int cartQty = Integer.parseInt(request.getParameter("qty"));
		CartServiceImpl cart = new CartServiceImpl();

		if (add == 1) {
			// Add Product into the cart
			cartQty += 1;
			if (cartQty <= avail) {
				cart.addProductToCart(uid, pid, 1);
			} else {
				response.sendRedirect("./AddtoCart?pid=" + pid + "&pqty=" + cartQty);
			}
		} else if (add == 0) {
			// Remove Product from the cart
			cart.removeProductFromCart(uid, pid);
		}
	}

	// Initialize shared variables
	CartServiceImpl cart = new CartServiceImpl();
	List<CartBean> cartItems = cart.getAllCartItems(userName);
	double totAmount = 0;
	String orderId = null;
	String prodId = null;

	// Calculate total amount and extract orderId and prodId
	for (CartBean item : cartItems) {
		if (orderId == null) {
			orderId = "someOrderId"; // Replace with logic to fetch order ID
		}
		if (prodId == null) {
			prodId = item.getProdId(); // Use the first product's ID
		}

		String currentProdId = item.getProdId();
		int prodQuantity = item.getQuantity();
		ProductBean product = new ProductServiceImpl().getProductDetails(currentProdId);
		double currAmount = product.getProdPrice() * prodQuantity;
		totAmount += currAmount;
	}
	%>

	<jsp:include page="header.jsp" />

	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold;">Cart Items</div>

	<div class="container">
		<!-- Discount Code Section -->
		<div class="discount-code-container" style="margin-bottom: 20px;">
			<form method="post" action="TransactionSrv">
				<div class="input-group">
					<!-- Pass the current total amount -->
					<input type="hidden" name="amount" value="<%=totAmount%>">
					<%
					if (orderId != null) { 
					%>
					<input type="hidden" name="orderid" value="<%=orderId%>">
					<%
					}
					if (prodId != null) { 
					%>
					<input type="hidden" name="prodid" value="<%=prodId%>">
					<%
					}
					%>
					<input type="text" name="discount_code" placeholder="Enter Discount Code"
						class="form-control" style="max-width: 300px;">
					<span class="input-group-btn">
						<button class="btn btn-success" type="submit">Apply</button>
					</span>
				</div>
			</form>
			<% if (request.getAttribute("error") != null) { %>
				<div class="alert alert-danger" style="margin-top: 10px;">
					<%= request.getAttribute("error") %>
				</div>
			<% } else if (request.getAttribute("message") != null) { %>
				<div class="alert alert-success" style="margin-top: 10px;">
					<%= request.getAttribute("message") %>
				</div>
			<% } %>
		</div>

		<table class="table table-hover">
			<thead style="background-color: #186188; color: white; font-size: 16px; font-weight: bold;">
				<tr>
					<th>Picture</th>
					<th>Products</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Add</th>
					<th>Remove</th>
					<th>Amount</th>
				</tr>
			</thead>
			<tbody style="background-color: white; font-size: 15px; font-weight: bold;">
				<%
				for (CartBean item : cartItems) {
					String currentProdId = item.getProdId();
					int prodQuantity = item.getQuantity();
					ProductBean product = new ProductServiceImpl().getProductDetails(currentProdId);
					double currAmount = product.getProdPrice() * prodQuantity;

					if (prodQuantity > 0) {
				%>
				<tr>
					<td><img src="./ShowImage?pid=<%=product.getProdId()%>" style="width: 50px; height: 50px;"></td>
					<td><%=product.getProdName()%></td>
					<td><%=product.getProdPrice()%></td>
					<td>
						<form method="post" action="./UpdateToCart">
							<input type="number" name="pqty" value="<%=prodQuantity%>" style="max-width: 70px;" min="0"> 
							<input type="hidden" name="pid" value="<%=product.getProdId()%>"> 
							<input type="submit" name="Update" value="Update" style="max-width: 80px;">
						</form>
					</td>
					<td><a href="cartDetails.jsp?add=1&uid=<%=userName%>&pid=<%=product.getProdId()%>&avail=<%=product.getProdQuantity()%>&qty=<%=prodQuantity%>"><i class="fa fa-plus"></i></a></td>
					<td><a href="cartDetails.jsp?add=0&uid=<%=userName%>&pid=<%=product.getProdId()%>&avail=<%=product.getProdQuantity()%>&qty=<%=prodQuantity%>"><i class="fa fa-minus"></i></a></td>
					<td><%=currAmount%></td>
				</tr>
				<% 
					} 
				} 
				%>

				<% if (session.getAttribute("discountAmount") != null) { %>
				<tr style="background-color: lightgrey; color: black;">
					<td colspan="6" style="text-align: center;">Discount Applied</td>
					<td>-<%= session.getAttribute("discountAmount") %></td>
				</tr>
				<% } %>

				<tr style="background-color: grey; color: white;">
					<td colspan="6" style="text-align: center;">Total Amount to Pay (in Rupees)</td>
					<td><%= session.getAttribute("finalAmount") != null ? session.getAttribute("finalAmount") : totAmount %></td>
				</tr>

				<%
				if (totAmount != 0) {
				%>
				<tr style="background-color: grey; color: white;">
					<td colspan="4" style="text-align: center;">
						<td>
							<form method="post">
								<button formaction="userHome.jsp" style="background-color: black; color: white;">
									Cancel
								</button>
							</form>
						</td>
						<td colspan="2" align="center">
							<form method="post">
								<button style="background-color: blue; color: white;"
									formaction="payment.jsp?amount=<%= session.getAttribute("finalAmount") != null ? session.getAttribute("finalAmount") : totAmount %>">
									Pay Now
								</button>
							</form>
						</td>
					</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>

	<%@ include file="footer.html"%>

</body>
</html>
