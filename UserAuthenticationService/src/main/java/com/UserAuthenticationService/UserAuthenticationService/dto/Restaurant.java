package com.UserAuthenticationService.UserAuthenticationService.dto;

import jakarta.persistence.*;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {


    private Long id;

    private String ownerEmail;

    private String restaurantName;

    private List<RestaurantItem> orderItems;

    public Restaurant() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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
