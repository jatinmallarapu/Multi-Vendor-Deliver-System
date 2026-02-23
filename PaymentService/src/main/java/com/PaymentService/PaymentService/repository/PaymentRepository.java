package com.PaymentService.PaymentService.repository;

import com.PaymentService.PaymentService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Payment findByOrderId(Long orderId);
}
