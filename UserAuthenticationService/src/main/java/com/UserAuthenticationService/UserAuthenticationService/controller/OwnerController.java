package com.UserAuthenticationService.UserAuthenticationService.controller;

import com.UserAuthenticationService.UserAuthenticationService.dto.DeliveryInformationDTO;
import com.UserAuthenticationService.UserAuthenticationService.dto.Restaurant;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantItem;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantOrderInformation;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantOrderInformationEvent;
import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.feign.DeliveryProxy;
import com.UserAuthenticationService.UserAuthenticationService.feign.RestaurantProxy;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {
    @Autowired
    private RestaurantProxy restaurantProxy;

    @Autowired
    private DeliveryProxy deliveryProxy;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String getMessage() {
        return "Hello Owner";
    }

    @GetMapping("/getuserid/{useremail}")
    public Long getUserId(@PathVariable("useremail") String useremail){
        User userdb = userRepository.findByEmail(useremail);
        return userdb.getId();
    }



    @PutMapping("/updaterestaurantname/{restaurantname}")
    public String updateRestaurantName(@RequestParam("email") String email, @PathVariable("restaurantname") String restaurantname ){
        return restaurantProxy.updateRestaurantName(email, restaurantname);
    }

    @GetMapping("/getrestaurantid/{owneremail}")
    @CircuitBreaker(name = "RESTAURANTSERVICE", fallbackMethod = "getRestaurantIdFallback")
    public Long getRestaurantId(@PathVariable("owneremail") String owneremail) {
        return restaurantProxy.getRestaurantId(owneremail);
    }

    @PostMapping("/additem/{restaurantId}")
    public List<RestaurantItem> addMenuItem(@PathVariable("restaurantId") Long restaurantId, @RequestBody List<RestaurantItem> items){
        return restaurantProxy.addMenuItem(restaurantId, items);
    }


    public Long getRestaurantIdFallback(String ownername, Throwable t) {
        System.out.println("Fallback for getRestaurantId called due to: " + t.getMessage());
        return -1L; // Return -1 as a fallback value
    }

    @GetMapping("/allorders/{restaurantid}")
    @CircuitBreaker(name = "RESTAURANTSERVICE", fallbackMethod = "getAllRestaurantOrdersFallback")
    public List<RestaurantOrderInformation> getAllRestaurantOrders(@PathVariable("restaurantid") Long restaurantid) {
        return restaurantProxy.getAllRestaurantOrders(restaurantid);
    }

    public List<RestaurantOrderInformation> getAllRestaurantOrdersFallback(Long restaurantid, Throwable t) {
        System.out.println("Fallback for getAllRestaurantOrders called due to: " + t.getMessage());
        return new ArrayList<>(); // Return empty list
    }

    @GetMapping("/prepare/{orderid}")
    @CircuitBreaker(name = "RESTAURANTSERVICE", fallbackMethod = "updateOrderAndInformUserFallback")
    public RestaurantOrderInformationEvent updateOrderAndInformUser(@PathVariable("orderid") Long orderid) {
        return restaurantProxy.updateOrderAndInformUser(orderid);
    }

    public RestaurantOrderInformationEvent updateOrderAndInformUserFallback(Long orderid, Throwable t) {
        System.out.println("Fallback for updateOrderAndInformUser called due to: " + t.getMessage());
        return new RestaurantOrderInformationEvent(); // Return empty event
    }

    @GetMapping("/prepared/{orderid}")
    public void updateOrderToPreparedAndInformUser(@PathVariable("orderid") Long orderid) {
        restaurantProxy.updateOrderToPreparedAndInformUser(orderid);
    }

    @GetMapping("/orderdelivering/{orderid}")
    @CircuitBreaker(name = "DELIVERY-SERVICE", fallbackMethod = "updateOrderAndInformUserAndAssignDriverFallback")
    public DeliveryInformationDTO updateOrderAndInformUserAndAssignDriver(@PathVariable("orderid") Long orderid) {
        return deliveryProxy.updateOrderAndInformUser(orderid);
    }

    public DeliveryInformationDTO updateOrderAndInformUserAndAssignDriverFallback(Long orderid, Throwable t) {
        System.out.println("Fallback for delivery service called due to: " + t.getMessage());
        return new DeliveryInformationDTO();
    }


    @GetMapping("/getrestaurantinfo/{restaurantid}")
    public Restaurant getRestaurantInfo(@PathVariable("restaurantid") Long restaurantid){
        return restaurantProxy.getRestaurantInfo(restaurantid);
    }

    //Used for websocket
    @GetMapping("/checkrestaurantowner/{restaurantid}")
    public String checkRestauarntOwner(@PathVariable("restaurantid") Long restaurantid){
        return restaurantProxy.getRestauarntOwnerEmail(restaurantid);
    }

    @DeleteMapping("/restaurant/deleteitem/{menuitemid}")
    public void deleteMenuItem(@PathVariable("menuitemid")Long menuitemid){
        restaurantProxy.deleteMenuItem(menuitemid);
    }
}
