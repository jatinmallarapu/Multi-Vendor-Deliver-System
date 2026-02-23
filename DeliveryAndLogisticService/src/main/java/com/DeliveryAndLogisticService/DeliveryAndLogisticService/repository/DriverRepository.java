package com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByAvailableTrue();
    Driver findByEmail(String email);
}