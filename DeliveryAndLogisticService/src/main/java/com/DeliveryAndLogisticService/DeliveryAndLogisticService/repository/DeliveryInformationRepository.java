package com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.DeliveryInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryInformationRepository extends JpaRepository<DeliveryInformation,Long> {
    public DeliveryInformation findByOrderId(Long orderId);

}
