package com.UserAuthenticationService.UserAuthenticationService.controller;

import com.UserAuthenticationService.UserAuthenticationService.dto.*;
import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.feign.OrderProxy;
import com.UserAuthenticationService.UserAuthenticationService.feign.RestaurantProxy;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private OrderProxy orderProxy;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantProxy restaurantProxy;


    @GetMapping
    public String getResponse() {
        return "Hello Customer";
    }

    @GetMapping("/getuserid/{useremail}")
    public Long getUserId(@PathVariable("useremail") String useremail){
        User userdb = userRepository.findByEmail(useremail);
        return userdb.getId();
    }

    @GetMapping("/restaurant/getallrestaurantinfo")
    public List<Restaurant> getAllRestuarant(){
        return restaurantProxy.getAllRestuarant();
    }

    @GetMapping("/restaurantinfo/{restaurantid}")
    public Restaurant getRestaurantInfo(@PathVariable("restaurantid") Long restaurantid){
        return restaurantProxy.getRestaurantInfo(restaurantid);
    }


    @PostMapping("/order")
    public ResponseEntity<Object> placeOrder(@RequestBody CreateOrderRequest request) {
        return orderProxy.placeOrder(request);
    }

    @GetMapping("/orders/{customerId}")
    public ResponseEntity<String> getOrder(@PathVariable("customerId") Long customerId) {
        return orderProxy.getOrder(customerId);
    }

    @GetMapping("/allorder/{customerid}")
    public List<Order> getAllCustomerOrders(@PathVariable("customerid")Long customerid){
        return orderProxy.getAllCustomerOrders(customerid);
    }

    @GetMapping("/orders/cancel/{orderid}")
    public Order cancelOrder(@PathVariable("orderid") Long orderid) {
        return orderProxy.cancelOrder(orderid);
    }

    @GetMapping("/orders/ratedriver/{orderid}")
    public ResponseEntity<String> rateDriver(@PathVariable("orderid") Long orderid,
            @RequestParam("rating") Double rating) {
        return orderProxy.rateDriver(orderid, rating);
    }

}
