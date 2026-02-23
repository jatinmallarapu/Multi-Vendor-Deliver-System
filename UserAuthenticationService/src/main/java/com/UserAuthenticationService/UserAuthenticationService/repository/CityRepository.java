package com.UserAuthenticationService.UserAuthenticationService.repository;

import com.UserAuthenticationService.UserAuthenticationService.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    public City findByName(String city);
}
