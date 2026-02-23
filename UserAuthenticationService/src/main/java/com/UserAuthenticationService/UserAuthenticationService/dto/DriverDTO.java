package com.UserAuthenticationService.UserAuthenticationService.dto;

public class DriverDTO {
    private String email;
    private boolean available;
    private double rating;
    public DriverDTO(){

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
}
