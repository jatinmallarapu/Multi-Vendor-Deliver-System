package com.DeliveryAndLogisticService.DeliveryAndLogisticService.service;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.CardDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.DeliveryInformationDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.OrderItemDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.DeliveryInformation;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.Driver;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.OrderStatus;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.event.RestaurantOrderInformationEvent;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DeliveryInformationRepository;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Transactional
public class DeliveryService{


    private final DriverRepository driverRepository;
    private final KafkaTemplate<String, DeliveryInformationDTO> kafkaTemplate;


    @Autowired
    private KafkaTemplate<String, RestaurantOrderInformationEvent> kafkaTemplateToUser;
    @Autowired
    DeliveryInformationRepository deliveryInformationRepository;

    public DeliveryService(DriverRepository driverRepository,
                           KafkaTemplate<String, DeliveryInformationDTO> kafkaTemplate) {
        this.driverRepository = driverRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public DeliveryInformationDTO updateOrderAndInformUser(Long orderId, String excludedDriver) {


        List<Driver> drivers = driverRepository.findByAvailableTrue()
                .stream()
                .filter(d -> !d.getEmail().equals(excludedDriver))
                .toList();;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<Driver>> futures = drivers.stream()
                .map(driver -> executor.submit(() -> scoreDriver(driver)))
                .toList();


        Driver bestDriver = futures.stream()
                .map(f -> {
                    try { return f.get(); } catch (Exception e) { return null; }
                })
                .filter(d -> d != null)
                .max(Comparator.comparingDouble(Driver::getRating))
                .orElseThrow();


        DeliveryInformation delivery = deliveryInformationRepository.findByOrderId(orderId);
        delivery.setStatus(OrderStatus.DELIVERING.toString());
        delivery.setDriver(bestDriver);
        deliveryInformationRepository.save(delivery);

        bestDriver.setAvailable(false);
        driverRepository.save(bestDriver);

        DeliveryInformationDTO diDTO=new DeliveryInformationDTO();
        diDTO.setOrderId(delivery.getOrderId());
        diDTO.setStatus(OrderStatus.DELIVERING.toString());
        diDTO.setDriverEmail(bestDriver.getEmail());
        diDTO.setPaymentStatus(delivery.getPaymentStatus());

        //send to user//to update order table
        kafkaTemplate.send("payment.failed", diDTO);
        //send to restaurant//to update restaurant table
        kafkaTemplate.send("payment.completed",diDTO);


        //send to user or customer updated information
        RestaurantOrderInformationEvent roiE=new RestaurantOrderInformationEvent();
        roiE.setOrderId(delivery.getOrderId());
        roiE.setCustomerId(delivery.getCustomerId());
        roiE.setCustomerId(delivery.getCustomerId());
        roiE.setStatus(delivery.getStatus());
        roiE.setRestaurantId(delivery.getRestaurantId());
        roiE.setAmount(delivery.getAmount());

        CardDTO cardDTO=new CardDTO();
        cardDTO.setCardNumber(delivery.getCard().getCardNumber());
        cardDTO.setCardType(delivery.getCard().getCardType());
        cardDTO.setCvv(delivery.getCard().getCvv());
        roiE.setCard(cardDTO);

        List<OrderItemDTO> dtoList = delivery.getOrderItemList().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setItemName(item.getItemName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();
        roiE.setOrderItemList(dtoList);
        roiE.setPaymentStatus(delivery.getPaymentStatus());
        roiE.setDriverEmail(bestDriver.getEmail());

        kafkaTemplateToUser.send("order.notification", roiE);

        executor.shutdown();

        return diDTO;
    }


    private Driver scoreDriver(Driver driver) {
// route optimization placeholder
        return driver;
    }

    public DeliveryInformationDTO updateOrderStatusToDeliveredAndInformToUser(Long orderid) {
        DeliveryInformation deliveryInformation = deliveryInformationRepository.findByOrderId(orderid);
        deliveryInformation.setStatus(OrderStatus.DELIVERED.toString());
        deliveryInformationRepository.save(deliveryInformation);

        String driverAssigned=deliveryInformation.getDriver().getEmail();
        Driver driver = driverRepository.findByEmail(driverAssigned);
        driver.setAvailable(true);
        double newRating = driver.getRating() + 0.3;
        if (newRating > 5.0) {
            newRating = 5.0; // clamp to max rating
        }
        driver.setRating(newRating);
        driverRepository.save(driver);

        DeliveryInformationDTO diDTO=new DeliveryInformationDTO();
        diDTO.setOrderId(deliveryInformation.getOrderId());
        diDTO.setStatus(deliveryInformation.getStatus());
        diDTO.setDriverEmail(driverAssigned);
        diDTO.setPaymentStatus(deliveryInformation.getPaymentStatus());

        kafkaTemplate.send("payment.failed",diDTO);
        kafkaTemplate.send("payment.completed",diDTO);


        //send to user or customer updated information
        RestaurantOrderInformationEvent roiE=new RestaurantOrderInformationEvent();
        roiE.setOrderId(deliveryInformation.getOrderId());
        roiE.setCustomerId(deliveryInformation.getCustomerId());
        roiE.setStatus(deliveryInformation.getStatus());
        roiE.setRestaurantId(deliveryInformation.getRestaurantId());
        roiE.setAmount(deliveryInformation.getAmount());

        CardDTO cardDTO=new CardDTO();
        cardDTO.setCardNumber(deliveryInformation.getCard().getCardNumber());
        cardDTO.setCardType(deliveryInformation.getCard().getCardType());
        cardDTO.setCvv(deliveryInformation.getCard().getCvv());
        roiE.setCard(cardDTO);

        List<OrderItemDTO> dtoList = deliveryInformation.getOrderItemList().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setItemName(item.getItemName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();
        roiE.setOrderItemList(dtoList);
        roiE.setPaymentStatus(deliveryInformation.getPaymentStatus());
        roiE.setDriverEmail(driverAssigned);

        kafkaTemplateToUser.send("order.notification", roiE);

        return diDTO;
    }

    public String cancelOrderDelivery(Long orderid) {
        DeliveryInformation diDB = deliveryInformationRepository.findByOrderId(orderid);
        if (diDB == null) {
            return "Error: No delivery found with orderId " + orderid;
        }
        Driver driverDB = diDB.getDriver();
        System.out.println(driverDB.getEmail()+" "+driverDB.getRating());
        String name = driverDB.getEmail();
        driverDB.setRating(driverDB.getRating()-0.2);
        driverDB.setAvailable(true);
        driverRepository.save(driverDB);
        updateOrderAndInformUser(orderid, name);
        return "Cancelled Delivery and Your current rating is "+driverDB.getRating();
    }
}
