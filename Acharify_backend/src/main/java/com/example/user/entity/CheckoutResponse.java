package com.example.user.entity;

public class CheckoutResponse {

    private Long orderId;
    private String status;
    private String message;

    public CheckoutResponse(Long orderId, String status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

