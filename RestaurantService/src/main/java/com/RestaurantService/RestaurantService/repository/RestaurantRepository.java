package com.RestaurantService.RestaurantService.repository;

import com.RestaurantService.RestaurantService.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    public Restaurant findByOwnerEmail(String email);
}
