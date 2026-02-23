package com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.CardDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.OrderItemDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="delivery_information")
public class DeliveryInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long customerId;
    private String status;
    private Long restaurantId;
    private BigDecimal amount;

    @Embedded
    private CardDTO card;

    @ElementCollection
    @CollectionTable(
            name = "delivery_order_items",
            joinColumns = @JoinColumn(name = "order_info_id")
    )
    private List<OrderItemDTO> orderItemList;
    private String paymentStatus; // SUCCESS, FAILED

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public DeliveryInformation(Long id, Long orderId, Long customerId, String status, Long restaurantId, BigDecimal amount, CardDTO card, List<OrderItemDTO> orderItemList, String paymentStatus, Driver driver) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.restaurantId = restaurantId;
        this.amount = amount;
        this.card = card;
        this.orderItemList = orderItemList;
        this.paymentStatus = paymentStatus;
        this.driver = driver;
    }
    public DeliveryInformation(){

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
