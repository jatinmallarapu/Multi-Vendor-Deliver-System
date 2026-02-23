package com.UserAuthenticationService.UserAuthenticationService.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)  // For audit logging
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Use Long instead of Integer for scalability

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;  // Changed from 'secondname' to standard 'lastname'

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;  // Required for drivers/restaurant owners

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)  // CRITICAL: Store as "CUSTOMER", "ADMIN" etc.
    @Column(nullable = false)
    private Role role;

    // ✅ Relationship: User -> Addresses (One user can have multiple addresses)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    // ✅ Relationship: User -> City (Many users belong to one city)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    // ✅ Device tracking for JWT refresh tokens
    private String deviceId;
    private String deviceType;  // "WEB", "ANDROID", "IOS"

    // ✅ Account status
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isEmailVerified = false;

    @Column(nullable = false)
    private Boolean isPhoneVerified = false;

    // ✅ Audit fields (auto-populated)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ✅ Soft delete support
    private LocalDateTime deletedAt;

    public User(Long id, String firstname, String lastname, String email, String phone, String password, Role role, List<Address> addresses, City city, String deviceId, String deviceType, Boolean isActive, Boolean isEmailVerified, Boolean isPhoneVerified, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.addresses = addresses;
        this.city = city;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.isPhoneVerified = isPhoneVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
    public User(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
