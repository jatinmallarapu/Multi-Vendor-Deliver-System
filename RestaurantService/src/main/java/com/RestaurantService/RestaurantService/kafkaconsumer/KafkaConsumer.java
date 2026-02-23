package com.RestaurantService.RestaurantService.kafkaconsumer;

import com.RestaurantService.RestaurantService.dto.CardDTO;
import com.RestaurantService.RestaurantService.dto.OrderItemDTO;
import com.RestaurantService.RestaurantService.dto.OrderStatus;
import com.RestaurantService.RestaurantService.entity.RestaurantItem;
import com.RestaurantService.RestaurantService.entity.RestaurantOrderInformation;
import com.RestaurantService.RestaurantService.event.PaymentConfirmedEvent;
import com.RestaurantService.RestaurantService.repository.RestaurantItemRepository;
import com.RestaurantService.RestaurantService.repository.RestaurantOrderInformationRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    @Autowired
    RestaurantOrderInformationRespository restaurantOrderInformationRespository;

    @Autowired
    RestaurantItemRepository restaurantItemRepository;

    @Transactional
    @KafkaListener(topics = "payment.completed", groupId = "restaurant-group")
    public void consumePaymentConfirmed(PaymentConfirmedEvent event) {
        // Log received event for debugging
        System.out.println("Processing payment.completed for Order ID: " + event.getOrderId() + ", Status: "
                + event.getStatus() + ", Driver: " + event.getDriverEmail());

        // Find existing record to avoid duplicates
        RestaurantOrderInformation roi = restaurantOrderInformationRespository.findByOrderId(event.getOrderId());

        if (roi == null) {
            // Only create if it doesn't exist
            roi = new RestaurantOrderInformation();
            roi.setOrderId(event.getOrderId());
            roi.setCustomerId(event.getCustomerId());
            roi.setRestaurantId(event.getRestaurantId());
            roi.setAmount(event.getAmount());

            CardDTO cardDTO = new CardDTO();
            if (event.getCard() != null) {
                cardDTO.setCardNumber(event.getCard().getCardNumber());
                cardDTO.setCardType(event.getCard().getCardType());
                cardDTO.setCvv(event.getCard().getCvv());
            }
            roi.setCard(cardDTO);

            if (event.getOrderItemList() != null) {
                List<OrderItemDTO> dtoList = event.getOrderItemList().stream().map(item -> {
                    OrderItemDTO itemDto = new OrderItemDTO();
                    itemDto.setMenuItemId(item.getMenuItemId());
                    RestaurantItem restaurantItem = restaurantItemRepository.findByMenuItemId(item.getMenuItemId());
                    if (restaurantItem != null) {
                        itemDto.setItemName(restaurantItem.getItemName());
                    } else {
                        itemDto.setItemName(item.getItemName());
                    }
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                }).toList();
                roi.setOrderItemList(dtoList);
            }
        }

        // Always update these fields regardless of whether it's new or existing
        roi.setStatus(event.getStatus());
        roi.setPaymentStatus(event.getPaymentStatus());

        if (event.getDriverEmail() != null) {
            roi.setDriverEmail(event.getDriverEmail());
        }

        restaurantOrderInformationRespository.save(roi);
    }
}
