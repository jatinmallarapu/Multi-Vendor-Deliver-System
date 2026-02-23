package com.RestaurantService.RestaurantService.controller;

import com.RestaurantService.RestaurantService.entity.Restaurant;
import com.RestaurantService.RestaurantService.entity.RestaurantItem;
import com.RestaurantService.RestaurantService.entity.RestaurantOrderInformation;
import com.RestaurantService.RestaurantService.event.RestaurantOrderInformationEvent;
import com.RestaurantService.RestaurantService.repository.RestaurantItemRepository;
import com.RestaurantService.RestaurantService.repository.RestaurantOrderInformationRespository;
import com.RestaurantService.RestaurantService.repository.RestaurantRepository;
import com.RestaurantService.RestaurantService.service.RestaurantService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@RestController
@RequestMapping("restaurant")
public class RestaurantController {

    @Autowired
    RestaurantItemRepository restaurantItemRepository;

    @Autowired
    RestaurantOrderInformationRespository restaurantOrderInformationRespository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/restaurantcheck/{restaurantid}/itemcheck/{orderitemid}")
    @Cacheable(value = "restaurantItemCache", key = "#restaurantid + '-' + #orderitemid")
    public RestaurantItem checkIfRestaurantItemExists(
            @PathVariable Long restaurantid,
            @PathVariable Long orderitemid) {

        Restaurant restaurant = restaurantRepository.findById(restaurantid).orElse(null);

        if (restaurant == null) {
            return null;
        }

        // Loop through items
        for (RestaurantItem item : restaurant.getOrderItems()) {
            if (item.getMenuItemId().equals(orderitemid)) {
                return item;
            }
        }

        return null;
    }

    @GetMapping("/check/{restaurantid}")
    @Cacheable(value = "restaurantExistsCache", key = "#restaurantid")
    public String checkIfRestaurantExists(@PathVariable Long restaurantid) {
        Restaurant restaurant = restaurantRepository.findById(restaurantid).orElse(null);

        if (restaurant != null) {
            return "Restaurant Found";
        } else {
            return "Restaurant Not Found";
        }
    }

    @GetMapping("/allorders/{restaurantid}")
    public List<RestaurantOrderInformation> getAllRestaurantOrders(@PathVariable("restaurantid") Long restaurantid) {
        return restaurantOrderInformationRespository.findAllByRestaurantId(restaurantid);
    }

    @GetMapping("/getorder/{orderid}")
    public RestaurantOrderInformation getRestauarantOrderByOrderId(@PathVariable Long orderid) {
        return restaurantOrderInformationRespository.findByOrderId(orderid);
    }

    @GetMapping("/updateorderpreparing/{orderid}")
    public RestaurantOrderInformationEvent updateOrderAndInformUser(@PathVariable("orderid") Long orderid) {
        System.out.println("******");
        return restaurantService.updateOrderAndInformUser(orderid);
    }

    @GetMapping("/updateorderprepared/{orderid}")
    public void updateOrderToPreparedAndInformUser(@PathVariable("orderid") Long orderid) {
        restaurantService.updateOrderToPreparedAndInformUser(orderid);
    }

    @GetMapping("/getrestaurantid/{ownerEmail}")
    @Cacheable(value = "restaurantIdCache", key = "#ownerEmail")
    public Long getRestaurantId(@PathVariable("ownerEmail") String ownerEmail) {
        Restaurant restaurant = restaurantRepository.findByOwnerEmail(ownerEmail);
        if (restaurant == null) {
            return -1L;
        }
        return restaurant.getId();
    }

    @PostMapping("/additem/{restaurantId}")
    public List<RestaurantItem> addMenuItem(@PathVariable("restaurantId") Long restaurantId, @RequestBody List<RestaurantItem> items) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found");
        }

        for (int i = 0; i < items.size(); i++) {
            RestaurantItem item = items.get(i);
            item.setRestaurant(restaurant);
            restaurantItemRepository.save(item);
        }

        return restaurant.getOrderItems();
    }

    @PostMapping("/add/onwerinfo/{email}")
    public String addOnwerInfo(@PathVariable("email") String email){
        Restaurant restaurant=new Restaurant();
        restaurant.setOwnerEmail(email);
        restaurantRepository.save(restaurant);
        return "Onwer resgistered";
    }

    @PutMapping("/updaterestaurantname/{restaurantname}")
    public String updateRestaurantName(@RequestParam("email") String email, @PathVariable("restaurantname") String restaurantname ){
        Restaurant restaurant = restaurantRepository.findByOwnerEmail(email);
        restaurant.setRestaurantName(restaurantname);
        restaurantRepository.save(restaurant);
        return "Your Restaurant name is set to "+restaurant.getRestaurantName();
    }


    @GetMapping("/checkrestauarntid/{restauarantid}")
    public String getRestauarntOwnerEmail(@PathVariable("restauarantid") Long restauarantid){
        Restaurant restaurant = restaurantRepository.findById(restauarantid).get();
        return restaurant.getOwnerEmail();
    }

    //for user controller
    @GetMapping("/getrestuarantinfo/{restaurantid}")
    public Restaurant getRestaurantInfo(@PathVariable("restaurantid") Long restaurantid){
        return restaurantRepository.findById(restaurantid).get();
    }

    @GetMapping("/getallrestaurantinfo")
    public List<Restaurant> getAllRestuarant(){
        return restaurantRepository.findAll();
    }

    @DeleteMapping("/deleteitem/{menuitemid}")
    @Transactional
    public void deleteMenuItem(@PathVariable("menuitemid")Long menuitemid){
        RestaurantItem restaurantItem = restaurantItemRepository.findByMenuItemId(menuitemid);
        restaurantItemRepository.delete(restaurantItem);

    }

}
