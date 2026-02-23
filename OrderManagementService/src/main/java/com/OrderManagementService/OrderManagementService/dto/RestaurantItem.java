package com.OrderManagementService.OrderManagementService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;

import java.math.BigDecimal;

public class RestaurantItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long menuItemId;
    private BigDecimal price;

    private Restaurant restaurant;

    public RestaurantItem(Long id, Long menuItemId, BigDecimal price, Restaurant restaurant) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.price = price;
        this.restaurant = restaurant;
    }

    public RestaurantItem() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
