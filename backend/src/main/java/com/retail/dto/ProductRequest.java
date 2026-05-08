package com.retail.dto;

import java.math.BigDecimal;

/**
 * DTO for product creation and update requests.
 * Contains product details for admin operations.
 */
public class ProductRequest {

    private String productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private Integer stockQuantity;

    // Default constructor
    public ProductRequest() {
    }

    // Parameterized constructor
    public ProductRequest(String productId, String productName, String category, 
                         BigDecimal price, String imageUrl, Integer stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}