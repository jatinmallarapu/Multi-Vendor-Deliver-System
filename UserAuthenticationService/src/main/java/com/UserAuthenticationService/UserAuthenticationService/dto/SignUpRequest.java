package com.UserAuthenticationService.UserAuthenticationService.dto;

import com.UserAuthenticationService.UserAuthenticationService.entities.Role;
import lombok.Data;

import java.util.List;


@Data
public class SignUpRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private List<AddressCreateDTO> address;
    private CityDto cityDto;
    private String deviceType;

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

    public List<AddressCreateDTO> getAddress() {
        return address;
    }

    public void setAddress(List<AddressCreateDTO> address) {
        this.address = address;
    }

    public CityDto getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDto cityDto) {
        this.cityDto = cityDto;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
