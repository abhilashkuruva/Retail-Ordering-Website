package com.retail.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDTO {
    private Long orderId;
    private String customerName;
    private String customerEmail;
    private int totalItems;
    private String productsSummary;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private LocalDateTime orderDate;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(Long orderId, String customerName, String customerEmail, 
                          int totalItems, String productsSummary, BigDecimal totalAmount,
                          String paymentStatus, String orderStatus, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalItems = totalItems;
        this.productsSummary = productsSummary;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getProductsSummary() {
        return productsSummary;
    }

    public void setProductsSummary(String productsSummary) {
        this.productsSummary = productsSummary;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}