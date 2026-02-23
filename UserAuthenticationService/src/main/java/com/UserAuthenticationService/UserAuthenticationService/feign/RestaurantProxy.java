package com.UserAuthenticationService.UserAuthenticationService.feign;

import com.UserAuthenticationService.UserAuthenticationService.dto.Restaurant;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantItem;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantOrderInformation;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantOrderInformationEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "RESTAURANTSERVICE")
public interface RestaurantProxy {

    @PostMapping("/restaurant/add/onwerinfo/{email}")
    public String addOnwerInfo(@PathVariable("email") String email);

    @PutMapping("/restaurant/updaterestaurantname/{restaurantname}")
    public String updateRestaurantName(@RequestParam("email") String email, @PathVariable("restaurantname") String restaurantname );




    @GetMapping("/restaurant/getrestaurantid/{owneremail}")
    public Long getRestaurantId(@PathVariable("owneremail") String owneremail);

    @GetMapping("/restaurant/allorders/{restaurantid}")
    public List<RestaurantOrderInformation> getAllRestaurantOrders(@PathVariable("restaurantid") Long restaurantid);

    @GetMapping("/restaurant/updateorderpreparing/{orderid}")
    public RestaurantOrderInformationEvent updateOrderAndInformUser(@PathVariable("orderid") Long orderid);

    @GetMapping("/restaurant/updateorderprepared/{orderid}")
    public void updateOrderToPreparedAndInformUser(@PathVariable("orderid") Long orderid);

    @PostMapping("/restaurant/additem/{restaurantId}")
    public List<RestaurantItem> addMenuItem(@PathVariable("restaurantId") Long restaurantId, @RequestBody List<RestaurantItem> items);

    @GetMapping("/restaurant/checkrestauarntid/{restauarantid}")
    public String getRestauarntOwnerEmail(@PathVariable("restauarantid") Long restauarantid);

    //user to view the restuarant
    @GetMapping("/restaurant/getrestuarantinfo/{restaurantid}")
    public Restaurant getRestaurantInfo(@PathVariable("restaurantid") Long restaurantid);

    @GetMapping("/restaurant/getallrestaurantinfo")
    public List<Restaurant> getAllRestuarant();

    @DeleteMapping("/restaurant/deleteitem/{menuitemid}")
    public void deleteMenuItem(@PathVariable("menuitemid")Long menuitemid);
}
