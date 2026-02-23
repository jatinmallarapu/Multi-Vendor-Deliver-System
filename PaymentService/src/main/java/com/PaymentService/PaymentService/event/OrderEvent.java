package com.PaymentService.PaymentService.event;

import com.PaymentService.PaymentService.dto.CardDTO;
import com.PaymentService.PaymentService.dto.OrderItemDTO;
import com.PaymentService.PaymentService.dto.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class OrderEvent {
    private Long orderId;
    private OrderStatus status;
    private Long customerId;
    private Long restaurantId;
    private BigDecimal amount;
    private CardDTO card;
    private List<OrderItemDTO> orderItemList;

    public OrderEvent(Long orderId, OrderStatus status, Long customerId, Long restaurantId, BigDecimal amount,
            CardDTO card, List<OrderItemDTO> orderItemList) {
        this.orderId = orderId;
        this.status = status;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.amount = amount;
        this.card = card;
        this.orderItemList = orderItemList;
    }

    public OrderEvent() {

    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
}
