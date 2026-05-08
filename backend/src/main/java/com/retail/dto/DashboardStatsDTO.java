package com.retail.dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private long totalProducts;
    private long totalOrders;
    private long totalItemsSold;
    private long totalCustomers;
    private BigDecimal totalRevenue;
    private long totalInventoryUnits;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(long totalProducts, long totalOrders, long totalItemsSold, 
                             long totalCustomers, BigDecimal totalRevenue, long totalInventoryUnits) {
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalItemsSold = totalItemsSold;
        this.totalCustomers = totalCustomers;
        this.totalRevenue = totalRevenue;
        this.totalInventoryUnits = totalInventoryUnits;
    }

    // Getters and Setters
    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(long totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalInventoryUnits() {
        return totalInventoryUnits;
    }

    public void setTotalInventoryUnits(long totalInventoryUnits) {
        this.totalInventoryUnits = totalInventoryUnits;
    }
}