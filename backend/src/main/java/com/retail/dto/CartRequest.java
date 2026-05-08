package com.retail.dto;

/**
 * DTO for cart operations.
 * Contains user ID, product ID and quantity for adding/updating cart items.
 */
public class CartRequest {

    private Long userId;
    private Long productId;
    private Integer quantity;

    // Default constructor
    public CartRequest() {
    }

    // Parameterized constructor
    public CartRequest(Long userId, Long productId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
