package com.UserAuthenticationService.UserAuthenticationService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private String landmark;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    // For delivery: latitude and longitude
    private Double latitude;
    private Double longitude;


    @Enumerated(EnumType.STRING)
    private AddressType type;  // HOME, WORK, OTHER

    public Address(Long id, User user, String addressLine1, String addressLine2, String landmark, String pincode, String city, String state, Double latitude, Double longitude, AddressType type) {
        this.id = id;
        this.user = user;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.landmark = landmark;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public Address(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
    }
}