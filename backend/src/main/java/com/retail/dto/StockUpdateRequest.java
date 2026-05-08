package com.retail.dto;

/**
 * DTO for stock update requests.
 * Used by admin to increase or decrease product inventory.
 */
public class StockUpdateRequest {

    private Integer stockQuantity;

    // Default constructor
    public StockUpdateRequest() {
    }

    // Parameterized constructor
    public StockUpdateRequest(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}