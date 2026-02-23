package com.DeliveryAndLogisticService.DeliveryAndLogisticService.kafkaConfig;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.CardDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.OrderItemDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.DeliveryInformation;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.Driver;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.OrderStatus;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.event.RestaurantOrderInformationEvent;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DeliveryInformationRepository;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DriverRepository;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.service.DeliveryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryConsumer {

    private final DeliveryService deliveryService;

    @Autowired
    DeliveryInformationRepository deliveryInformationRepository;

    @Autowired
    DriverRepository driverRepository;

    public DeliveryConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Transactional
    @KafkaListener(topics = {"payment.completed", "order.deliver"}, groupId = "delivery-group")
    public void consumePaymentConfirmed(RestaurantOrderInformationEvent event) throws InterruptedException {

        if (event.getRating() != null) {
            DeliveryInformation diDB = deliveryInformationRepository.findByOrderId(event.getOrderId());
            if (diDB != null && diDB.getDriver() != null) {
                Driver driverDB = diDB.getDriver();
                driverDB.setRating((driverDB.getRating() + event.getRating()) / 2);
                driverRepository.save(driverDB);
            }
        } else {
            // Find existing or create new
            DeliveryInformation deliveryInformation = deliveryInformationRepository.findByOrderId(event.getOrderId());

            if (deliveryInformation == null) {
                deliveryInformation = new DeliveryInformation();
                deliveryInformation.setOrderId(event.getOrderId());
                deliveryInformation.setCustomerId(event.getCustomerId());
                deliveryInformation.setRestaurantId(event.getRestaurantId());
                deliveryInformation.setAmount(event.getAmount());
                deliveryInformation.setPaymentStatus(event.getPaymentStatus());

                CardDTO cardDTO = new CardDTO();
                if (event.getCard() != null) {
                    cardDTO.setCardNumber(event.getCard().getCardNumber());
                    cardDTO.setCardType(event.getCard().getCardType());
                    cardDTO.setCvv(event.getCard().getCvv());
                }
                deliveryInformation.setCard(cardDTO);

                if (event.getOrderItemList() != null) {
                    List<OrderItemDTO> dtoList = event.getOrderItemList().stream().map(item -> {
                        OrderItemDTO itemDto = new OrderItemDTO();
                        itemDto.setMenuItemId(item.getMenuItemId());
                        itemDto.setItemName(item.getItemName());
                        itemDto.setQuantity(item.getQuantity());
                        itemDto.setPrice(item.getPrice());
                        return itemDto;
                    }).toList();
                    deliveryInformation.setOrderItemList(dtoList);
                }
            }

            // Always update status and driver if present
            deliveryInformation.setStatus(event.getStatus());
            if (event.getDriverEmail() != null) {
                Driver driver = driverRepository.findByEmail(event.getDriverEmail());
                if (driver != null) {
                    deliveryInformation.setDriver(driver);
                }
            }
            deliveryInformationRepository.save(deliveryInformation);
        }
    }
}
