package com.PaymentService.PaymentService.config;

import com.PaymentService.PaymentService.event.OrderEvent;
import com.PaymentService.PaymentService.dto.OrderItemDTO;
import com.PaymentService.PaymentService.service.PaymentService;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaConsumerConfig {


    private final PaymentService paymentService;

    public KafkaConsumerConfig(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "order.created", groupId = "payment-group")
    public void consumeOrderPlaced(OrderEvent event) {
        System.out.println("*******");
        System.out.println(event.getOrderId());
        System.out.println(event.getAmount());
        System.out.println(event.getCard().getCardType());
        System.out.println(event.getCard().getCardNumber());
        System.out.println(event.getCard().getCvv());
        for (OrderItemDTO item : event.getOrderItemList()) {
            System.out.println(
                    "MenuItemId: " + item.getMenuItemId() +
                            "Item name: "+item.getItemName()+
                            ", Qty: " + item.getQuantity() +
                            ", Price: " + item.getPrice()
            );
        }
        System.out.println("***************");
        paymentService.processOrderPayment(event);
    }
}