package com.OrderManagementService.OrderManagementService.feign;

import com.OrderManagementService.OrderManagementService.dto.RestaurantItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESTAURANTSERVICE")
public interface RestaurantProxy {

        @GetMapping("/restaurant/restaurantcheck/{restaurantid}/itemcheck/{orderitemid}")
        public RestaurantItem checkIfRestaurantItemExists(
                        @PathVariable("restaurantid") Long restaurantid,
                        @PathVariable("orderitemid") Long orderitemid);

        @GetMapping("/restaurant/check/{restaurantid}")
        public String checkIfRestaurantExists(@PathVariable("restaurantid") Long restaurantid);
}
