package com.OrderManagementService.OrderManagementService.dto;

import jakarta.persistence.*;

import java.util.List;

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String onwername;
    private String restaurantName;

    private List<RestaurantItem> orderItems;

    public Restaurant(Long id, String onwername, String restaurantName, List<RestaurantItem> orderItems) {
        this.id = id;
        this.onwername = onwername;
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
    }
    public Restaurant(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOnwername() {
        return onwername;
    }

    public void setOnwername(String onwername) {
        this.onwername = onwername;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<RestaurantItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<RestaurantItem> orderItems) {
        this.orderItems = orderItems;
    }
}
