package com.RestaurantService.RestaurantService.dto;

import jakarta.persistence.*;
import java.io.Serializable;

import java.math.BigDecimal;


@Embeddable
public class OrderItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
