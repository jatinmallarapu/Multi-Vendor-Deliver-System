package com.UserAuthenticationService.UserAuthenticationService.dto;

import java.math.BigDecimal;
import java.util.List;

public class DeliveryInformation {
    private Long id;
    private Long orderId;
    private Long customerId;
    private String status;
    private Long restaurantId;
    private BigDecimal amount;
    private CardDTO card;
    private List<OrderItemDTO> orderItemList;
    private String paymentStatus;
    private Driver driver;

    public DeliveryInformation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public List<OrderItemDTO> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemDTO> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
