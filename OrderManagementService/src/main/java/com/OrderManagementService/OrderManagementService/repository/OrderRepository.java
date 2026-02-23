package com.OrderManagementService.OrderManagementService.repository;

import com.OrderManagementService.OrderManagementService.entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findById(Long id);
    Optional<Order> findTopByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Order> findAllByCustomerId(Long customerid);
}

