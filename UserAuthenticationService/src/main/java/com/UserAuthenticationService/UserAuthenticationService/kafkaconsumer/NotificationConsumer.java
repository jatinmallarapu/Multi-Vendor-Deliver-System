package com.UserAuthenticationService.UserAuthenticationService.kafkaconsumer;

import com.UserAuthenticationService.UserAuthenticationService.dto.OrderItemDTO;
import com.UserAuthenticationService.UserAuthenticationService.dto.RestaurantOrderInformationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "order.notification", groupId = "user-group")
    public void consumeEvent(RestaurantOrderInformationEvent event){
        // Send notification to specific customer via WebSocket
        if (event.getCustomerId() != null) {
            messagingTemplate.convertAndSend("/topic/customer/", event);
        }

        // Keep console logging for debugging
        if (event.getOrderItemList() == null) {
            System.out.println("ORDER ID: "+event.getOrderId()+", ORDER STATUS: "+event.getStatus()+", CUSTOMER ID: "+event.getCustomerId()
                    +", RESTAURANT ID: "+event.getRestaurantId()+", TOTAL AMOUNT: "+event.getAmount()+" PAYMENT STATUS: "+event.getPaymentStatus()
                    +", DRIVER EMAIL: "+(event.getDriverEmail() != null ? event.getDriverEmail() : "Not Assigned"));

        }
        else{
            for(OrderItemDTO item: event.getOrderItemList()){
                System.out.println("RESTAURANT ID: "+event.getRestaurantId()+", MENU ITEM ID: "+item.getMenuItemId()+"ITEM NAME: "+item.getItemName()+", QUANTITY: "+item.getQuantity()+", PRICE: "+item.getPrice());
            }
            System.out.println("ORDER ID: "+event.getOrderId()+", ORDER STATUS: "+event.getStatus()+", CUSTOMER ID: "+event.getCustomerId()
                    +", RESTAURANT ID: "+event.getRestaurantId()+", TOTAL AMOUNT: "+event.getAmount()+" PAYMENT STATUS: "+event.getPaymentStatus()
                    +", DRIVER EMAIL: "+(event.getDriverEmail() != null ? event.getDriverEmail() : "Not Assigned"));
        }

    }
}
