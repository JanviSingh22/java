package com.example.OrderUp.dto;

import com.example.OrderUp.entity.OrderStatus;

public class OrderResponse {
    private Long orderId;
    private int quantity;
    private String status;
    private Long productId;
    private String productName;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, int quantity, String status, Long productId, String productName) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.status = status;
        this.productId = productId;
        this.productName = productName;
    }

    public OrderResponse(Long id, int quantity, OrderStatus status, Long id1, String name) {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
