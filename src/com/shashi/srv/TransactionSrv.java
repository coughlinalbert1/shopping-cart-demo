package com.shashi.srv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shashi.beans.TransactionBean;
import com.shashi.service.TransService;
import com.shashi.service.impl.TransServiceImpl;
import com.shashi.utility.DBUtil;

@WebServlet("/TransactionSrv")
public class TransactionSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final TransService transService = new TransServiceImpl();

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                             throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");


        
        if (username == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again");
            return;
        }

        // Get the discount code, initial amount, order ID, and product ID from the request
        String discountCode = request.getParameter("discount_code");
        double initialAmount = Double.parseDouble(request.getParameter("amount"));
        String orderId = request.getParameter("orderid");
        String prodId = request.getParameter("prodid");


        // Check to see if orderId and prodId are null
        if (orderId == null || prodId == null) {
            // Set error message and forward to cartDetails.jsp
            request.setAttribute("error", "Invalid order details. Please try again.");
            RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");
            rd.forward(request, response);
            return;
        }

        // Check if the order exists in the database without using username
        try (Connection con = DBUtil.provideConnection()) {
            String query = "SELECT * FROM orders WHERE orderid = ? AND prodid = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, orderId);
            ps.setString(2, prodId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
            // Order does not exist
            request.setAttribute("error", "Order not found. Please check your order details.");
            RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");
            rd.forward(request, response);
            return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred.");
            RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");
            rd.forward(request, response);
            return;
        }

        double discountAmount = 0.0;
        double finalAmount = initialAmount;


        // Create a new transaction bean and set the order ID, product ID, and discount code
        // Populate a new transaction bean
        TransactionBean transaction = new TransactionBean(username, initialAmount);
        transaction.setOrderId(orderId);
        transaction.setProdId(prodId);
        transaction.setDiscountCode(discountCode);

        // Try the discount code and alter the transaction bean with the new price
        try {
            // Check if discount code was provided
            if (discountCode != null && !discountCode.trim().isEmpty()) {
                // Using the new functions to apply the discount 
                transaction = transService.applyDiscount(transaction, discountCode); // CHECK THIS I THINK WRONGGGG
                discountAmount = transaction.getDiscountAmount();
                finalAmount = transaction.getFinalAmount();
            }

            transaction.setTransAmount(finalAmount);
            transService.recordTransaction(transaction);
            session.setAttribute("finalAmount", finalAmount);
            session.setAttribute("discountAmount", discountAmount);
            request.setAttribute("message", "Discount applied successfully!");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid discount code: " + e.getMessage());
            session.setAttribute("finalAmount", initialAmount);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred when processing the transaction");
            session.setAttribute("finalAmount", initialAmount);
        }


        RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
