package com.PaymentService.PaymentService.service;

import com.PaymentService.PaymentService.event.OrderEvent;
import com.PaymentService.PaymentService.dto.OrderItemDTO;
import com.PaymentService.PaymentService.event.PaymentConfirmedEvent;
import com.PaymentService.PaymentService.entity.Payment;
import com.PaymentService.PaymentService.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentConfirmedEvent> kafkaTemplate;

    public PaymentService(PaymentRepository paymentRepository,
            KafkaTemplate<String, PaymentConfirmedEvent> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // private final String PAYMENT_CONFIRMED_TOPIC = "payment.confirmed";

    @Transactional
    public void processOrderPayment(OrderEvent event) {
        // Simulate payment processing
        if (event.getStatus().toString().equals("CANCELLED")) {
            String status = event.getStatus().toString();
            Payment payment = paymentRepository.findByOrderId(event.getOrderId());
            payment.setStatus("CANCELLED");
            paymentRepository.save(payment);

            // Publish PaymentConfirmed event
            PaymentConfirmedEvent confirmedEvent = new PaymentConfirmedEvent();
            confirmedEvent.setOrderId(event.getOrderId());
            confirmedEvent.setCustomerId(event.getCustomerId());
            confirmedEvent.setAmount(event.getAmount());
            confirmedEvent.setPaymentStatus(status);

            confirmedEvent.setStatus(event.getStatus().toString());
            confirmedEvent.setRestaurantId(event.getRestaurantId());
            confirmedEvent.setCard(event.getCard());
            confirmedEvent.setOrderItemList(event.getOrderItemList());

            for (OrderItemDTO orderItem : confirmedEvent.getOrderItemList()) {
                System.out.println(
                        orderItem.getMenuItemId() + " "+orderItem.getItemName()+ " " + orderItem.getQuantity() + " " + orderItem.getPrice());
            }
            // to restaurantservice
            kafkaTemplate.send("payment.completed", confirmedEvent);
            // send notification to specific user
            kafkaTemplate.send("order.notification", confirmedEvent);
        } else {
            String status = simulatePayment() ? "SUCCESS" : "FAILED";

            Payment payment = new Payment();
            payment.setOrderId(event.getOrderId());
            payment.setCustomerId(event.getCustomerId());
            payment.setAmount(event.getAmount());
            payment.setPaymentMethod(event.getCard().getCardType().toString()); // can extend to Wallet/COD later
            payment.setStatus(status);

            paymentRepository.save(payment);

            // Publish PaymentConfirmed event
            PaymentConfirmedEvent confirmedEvent = new PaymentConfirmedEvent();
            confirmedEvent.setOrderId(event.getOrderId());
            System.out.println("confirmed event: " + confirmedEvent.getOrderId());
            confirmedEvent.setCustomerId(event.getCustomerId());
            confirmedEvent.setAmount(event.getAmount());
            confirmedEvent.setPaymentStatus(status);

            confirmedEvent.setStatus(event.getStatus().toString());
            confirmedEvent.setRestaurantId(event.getRestaurantId());
            confirmedEvent.setCard(event.getCard());
            confirmedEvent.setOrderItemList(event.getOrderItemList());

            for (OrderItemDTO orderItem : confirmedEvent.getOrderItemList()) {
                System.out.println(
                        orderItem.getMenuItemId() + " " + orderItem.getQuantity() + " " + orderItem.getPrice());
            }

            if (status.equals("SUCCESS")) {
                // to restaurantservice
                kafkaTemplate.send("payment.completed", confirmedEvent);
                // send notification to specific user
                kafkaTemplate.send("order.notification", confirmedEvent);
            } else if (status.equals("FAILED")) {
                // to orderservice
                System.out.println(confirmedEvent.getPaymentStatus());
                kafkaTemplate.send("payment.failed", confirmedEvent);
                // send notification to specific user
                kafkaTemplate.send("order.notification", confirmedEvent);
            }

        }

    }

    private boolean simulatePayment() {
        // Randomly approve/reject payment for demo
        return new Random().nextBoolean();
    }
}
