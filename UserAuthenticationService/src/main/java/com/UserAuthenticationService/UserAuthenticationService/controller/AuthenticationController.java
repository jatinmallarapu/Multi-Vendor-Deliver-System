package com.UserAuthenticationService.UserAuthenticationService.controller;


import com.UserAuthenticationService.UserAuthenticationService.dto.*;
import com.UserAuthenticationService.UserAuthenticationService.entities.City;
import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.feign.DeliveryProxy;
import com.UserAuthenticationService.UserAuthenticationService.feign.RestaurantProxy;
import com.UserAuthenticationService.UserAuthenticationService.repository.CityRepository;
import com.UserAuthenticationService.UserAuthenticationService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    UserService service;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    RestaurantProxy restaurantProxy;

    @Autowired
    DeliveryProxy deliveryProxy;


    @PostMapping("/signup")
    public User register(@RequestBody SignUpRequest signUpRequest){
        if(signUpRequest.getRole().toString().equals("RESTAURANT_OWNER")) {
            String onwerRegisteredStatus = restaurantProxy.addOnwerInfo(signUpRequest.getEmail());
            System.out.println(onwerRegisteredStatus);
        }
        else if(signUpRequest.getRole().toString().equals("DELIVERY_DRIVER")) {
            DriverDTO dto = new DriverDTO();
            dto.setEmail(signUpRequest.getEmail());
            dto.setAvailable(true);
            dto.setRating(5.0);
            String driverRegisteredStatus = deliveryProxy.addDriverInfo(dto);
            System.out.println(driverRegisteredStatus);
        }
        return service.register(signUpRequest);
    }

    @PostMapping("/signin")
    public JWTAuthenticationResponse login(@RequestBody SignInRequest signInRequest){
        try {

            return service.verifyUser(signInRequest);
        }catch (IllegalAccessException exception){
            JWTAuthenticationResponse jwtAuthenticationResponse=new JWTAuthenticationResponse();
            jwtAuthenticationResponse.setToken("Invalid User");
            jwtAuthenticationResponse.setRefreshToken("Invalid User");
            return jwtAuthenticationResponse;
        }
    }

    @PostMapping("/refresh")
    public JWTAuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return service.refreshToken(refreshTokenRequest);

    }

    @PostMapping("/cities")
    public City addCity(@RequestBody CityDto cityDto){
        City city=new City();
        city.setName(cityDto.getName());
        city.setState(cityDto.getState());
        return cityRepository.save(city);

    }

    @GetMapping("/citieslist")
    public List<City> getAllCities(){
        return cityRepository.findAll();
    }
}
