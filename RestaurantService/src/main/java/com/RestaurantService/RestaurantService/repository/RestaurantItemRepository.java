package com.RestaurantService.RestaurantService.repository;

import com.RestaurantService.RestaurantService.entity.Restaurant;
import com.RestaurantService.RestaurantService.entity.RestaurantItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantItemRepository extends JpaRepository<RestaurantItem,Long> {

    public RestaurantItem findByMenuItemId(Long menuItemId);
}
