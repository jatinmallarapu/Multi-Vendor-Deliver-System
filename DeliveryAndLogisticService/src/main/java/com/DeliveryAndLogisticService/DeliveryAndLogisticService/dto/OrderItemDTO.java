package com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;


@Embeddable
public class OrderItemDTO {

    private Long menuItemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemDTO() {}

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
