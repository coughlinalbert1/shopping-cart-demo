package com.shashi.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.shashi.utility.IDUtil;

@SuppressWarnings("serial")
public class TransactionBean implements Serializable {

	private String transactionId;

	private String userName;

	private Timestamp transDateTime;

	private double transAmount;

	private String discountCode;

	private double discountAmount;

	private double finalAmount;

	private String orderId; 

	private String prodId;  

	public TransactionBean() {
		super();
		this.transactionId = IDUtil.generateTransId();

		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		sdf.format(timestamp);

		this.transDateTime = timestamp;

		this.transAmount = 0.0;

		this.finalAmount = this.transAmount;
	}

	public TransactionBean(String userName, double transAmount) {
		super();
		this.userName = userName;
		this.transAmount = transAmount;

		this.transactionId = IDUtil.generateTransId();

		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		sdf.format(timestamp);

		this.transDateTime = timestamp;

		this.finalAmount = this.transAmount;

	}


	public TransactionBean(String transactionId, String userName, Timestamp transDateTime, double transAmount, 
						   String discountCode, double discountAmount, double finalAmount, String orderId, String prodId) {
		super();
		this.transactionId = transactionId;
		this.userName = userName;
		this.transDateTime = transDateTime;
		this.transAmount = transAmount;
		this.discountCode = discountCode;
		this.discountAmount = discountAmount;
		this.finalAmount = finalAmount;
		this.orderId = orderId;
		this.prodId = prodId;
	}

	// Getters and Setters
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getTransDateTime() {
		return transDateTime;
	}

	public void setTransDateTime(Timestamp transDateTime) {
		this.transDateTime = transDateTime;
	}

	public double getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(double transAmount) {
		this.transAmount = transAmount;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
}
