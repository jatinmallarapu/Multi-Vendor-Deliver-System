package com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private boolean available;
    private double rating;
    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<DeliveryInformation> deliveries;


    public Driver(Long id, String email, boolean available, double rating, List<DeliveryInformation> deliveries) {
        this.id = id;
        this.email = email;
        this.available = available;
        this.rating = rating;
        this.deliveries = deliveries;
    }

    public Driver(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<DeliveryInformation> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryInformation> deliveries) {
        this.deliveries = deliveries;
    }
}