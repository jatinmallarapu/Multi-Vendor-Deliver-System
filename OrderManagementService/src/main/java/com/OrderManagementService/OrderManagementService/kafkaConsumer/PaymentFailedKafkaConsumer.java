package com.OrderManagementService.OrderManagementService.kafkaConsumer;

import com.OrderManagementService.OrderManagementService.entity.Order;
import com.OrderManagementService.OrderManagementService.entity.OrderStatus;
import com.OrderManagementService.OrderManagementService.event.PaymentStatusEvent;
import com.OrderManagementService.OrderManagementService.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentFailedKafkaConsumer {


    @Autowired
    OrderRepository orderRepository;

    @KafkaListener(topics = "payment.failed", groupId = "order-group")
    @Transactional
    public void consumeOrderPlaced(PaymentStatusEvent event){
        System.out.println("******");
        System.out.println(event.getPaymentStatus());
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(event.getPaymentStatus().equals("FAILED")) {
            order.setStatus(OrderStatus.CANCELLED);
            System.out.println(order.getStatus());
            orderRepository.save(order);
        }
        else if(event.getPaymentStatus().equals("SUCCESS")){
            System.out.println("1******");
            order.setStatus(OrderStatus.CREATED);
            System.out.println(order.getStatus());
            orderRepository.save(order);
            if(event.getStatus().equals("PREPARING")){
                System.out.println("2******");
                order.setStatus(OrderStatus.PREPARING);
                System.out.println(order.getStatus());
                orderRepository.save(order);
            }
            if(event.getStatus().equals("PREPARED")){
                System.out.println("3******");
                order.setDriverEmail(event.getDriverEmail());
                OrderStatus orderStatus = OrderStatus.valueOf(event.getStatus());
                order.setStatus(orderStatus);
                System.out.println(order.getStatus());
                orderRepository.save(order);
            }
            if(event.getStatus().equals("DELIVERING")){
                System.out.println("4******");
                order.setDriverEmail(event.getDriverEmail());
                OrderStatus orderStatus = OrderStatus.valueOf(event.getStatus());
                order.setStatus(orderStatus);
                System.out.println(order.getStatus());
                orderRepository.save(order);
            }
            if(event.getStatus().equals("DELIVERED")){
                System.out.println("5******");
                order.setDriverEmail(event.getDriverEmail());
                OrderStatus orderStatus = OrderStatus.valueOf(event.getStatus());
                order.setStatus(orderStatus);
                System.out.println(order.getStatus());
                orderRepository.save(order);
            }




        }

    }
}
