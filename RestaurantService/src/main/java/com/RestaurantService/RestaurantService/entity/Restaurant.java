package com.RestaurantService.RestaurantService.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ownerEmail;

    private String restaurantName;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<RestaurantItem> orderItems;

    public Restaurant(Long id, String ownerEmail, String restaurantName, List<RestaurantItem> orderItems) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
    }

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
