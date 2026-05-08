package com.retail.dto;

/**
 * Generic DTO for API responses.
 * Used to return consistent response format across all endpoints.
 */
public class ApiResponse {

    private String message;
    private boolean success;
    private Object data;

    // Default constructor
    public ApiResponse() {
    }

    // Constructor with message and success
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Constructor with all fields
    public ApiResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}