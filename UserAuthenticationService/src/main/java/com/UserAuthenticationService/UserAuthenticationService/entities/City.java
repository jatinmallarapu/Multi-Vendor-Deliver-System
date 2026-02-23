package com.UserAuthenticationService.UserAuthenticationService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String state;

    private String country = "USA";

    // For service availability
    @Column(nullable = false)
    private Boolean isServiceable = true;

    @OneToMany(mappedBy = "city")
    @JsonIgnore
    private List<User> users;

    public City(Long id, String name, String state, String country, Boolean isServiceable, List<User> users) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.isServiceable = isServiceable;
        this.users = users;
    }
    public City(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getServiceable() {
        return isServiceable;
    }

    public void setServiceable(Boolean serviceable) {
        isServiceable = serviceable;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}