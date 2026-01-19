package com.example.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private String userName;
    private String supplierName;
    private LocalDateTime orderDate;
    private LocalDate expectedDeliveryDate;
    private String deliveryStatus;
    private List<OrderItemDTO> items;

    public OrderDTO(Long orderId, String userName, String supplierName,
                    LocalDateTime orderDate, LocalDate expectedDeliveryDate,
                    String deliveryStatus, List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.userName = userName;
        this.supplierName = supplierName;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.deliveryStatus = deliveryStatus;
        this.items = items;
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDate getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}

    // Getters & Setters
    
}

