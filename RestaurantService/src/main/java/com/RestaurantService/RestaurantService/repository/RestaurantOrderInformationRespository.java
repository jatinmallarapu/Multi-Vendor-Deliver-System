package com.RestaurantService.RestaurantService.repository;


import com.RestaurantService.RestaurantService.entity.RestaurantOrderInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantOrderInformationRespository extends JpaRepository<RestaurantOrderInformation,Long> {
    public RestaurantOrderInformation findByOrderId(Long orderid);
    public List<RestaurantOrderInformation> findAllByRestaurantId(Long restauarantid);
}
