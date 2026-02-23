package com.RestaurantService.RestaurantService.service;

import com.RestaurantService.RestaurantService.dto.CardDTO;
import com.RestaurantService.RestaurantService.dto.DeliveryInformationDTO;
import com.RestaurantService.RestaurantService.dto.OrderItemDTO;
import com.RestaurantService.RestaurantService.dto.OrderStatus;
import com.RestaurantService.RestaurantService.entity.Restaurant;
import com.RestaurantService.RestaurantService.entity.RestaurantOrderInformation;
import com.RestaurantService.RestaurantService.event.RestaurantOrderInformationEvent;
import com.RestaurantService.RestaurantService.repository.RestaurantOrderInformationRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RestaurantService {

    @Autowired
    RestaurantOrderInformationRespository restaurantOrderInformationRespository;

    @Autowired
    KafkaTemplate<String, RestaurantOrderInformationEvent> kafkaTemplate;

    @Autowired
    KafkaTemplate<String, DeliveryInformationDTO> kafkaTemplatePrepared;


    public RestaurantOrderInformationEvent updateOrderAndInformUser(Long orderId) {
        RestaurantOrderInformation roi = restaurantOrderInformationRespository.findByOrderId(orderId);

        RestaurantOrderInformationEvent roiE=new RestaurantOrderInformationEvent();
        if(roi!=null){
            if(roi.getStatus().equals("CANCELLED")){

                roiE.setOrderId(roi.getOrderId());
                roiE.setCustomerId(roi.getCustomerId());
                roiE.setCustomerId(roi.getCustomerId());
                roiE.setStatus(roi.getStatus());
                roiE.setRestaurantId(roi.getRestaurantId());
                roiE.setAmount(roi.getAmount());

                CardDTO cardDTO=new CardDTO();
                cardDTO.setCardNumber(roi.getCard().getCardNumber());
                cardDTO.setCardType(roi.getCard().getCardType());
                cardDTO.setCvv(roi.getCard().getCvv());
                roiE.setCard(cardDTO);

                List<OrderItemDTO> dtoList = roi.getOrderItemList().stream().map(item -> {
                    OrderItemDTO itemDto = new OrderItemDTO();
                    itemDto.setMenuItemId(item.getMenuItemId());
                    itemDto.setItemName(item.getItemName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                }).toList();
                roiE.setOrderItemList(dtoList);
                roiE.setPaymentStatus(roi.getPaymentStatus());

                kafkaTemplate.send("order.deliver",roiE);
                kafkaTemplate.send("order.notification",roiE);

            }
            else {
                roi.setStatus(OrderStatus.PREPARING.toString());


                roiE.setOrderId(roi.getOrderId());
                roiE.setCustomerId(roi.getCustomerId());
                roiE.setCustomerId(roi.getCustomerId());
                roiE.setStatus(roi.getStatus());
                roiE.setRestaurantId(roi.getRestaurantId());
                roiE.setAmount(roi.getAmount());

                CardDTO cardDTO = new CardDTO();
                cardDTO.setCardNumber(roi.getCard().getCardNumber());
                cardDTO.setCardType(roi.getCard().getCardType());
                cardDTO.setCvv(roi.getCard().getCvv());
                roiE.setCard(cardDTO);

                List<OrderItemDTO> dtoList = roi.getOrderItemList().stream().map(item -> {
                    OrderItemDTO itemDto = new OrderItemDTO();
                    itemDto.setMenuItemId(item.getMenuItemId());
                    itemDto.setItemName(item.getItemName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                }).toList();
                roiE.setOrderItemList(dtoList);
                roiE.setPaymentStatus(roi.getPaymentStatus());

                    /*
                    private Long orderId;
                    private Long customerId;
                    private String status;
                    private Long restaurantId;
                    private BigDecimal amount;
                    private CardDTO card;
                    private List<OrderItemDTO> orderItemList;
                    private String paymentStatus; // SUCCESS, FAILED
                     */
                System.out.println("******");
                System.out.println(roi.getStatus());
                restaurantOrderInformationRespository.save(roi);
                kafkaTemplate.send("payment.failed", roiE);
                kafkaTemplate.send("order.deliver", roiE);
                kafkaTemplate.send("order.notification",roiE);
            }
        }
        return roiE;
    }

    public void updateOrderToPreparedAndInformUser(Long orderid) {
        RestaurantOrderInformation roi = restaurantOrderInformationRespository.findByOrderId(orderid);
        if(roi!=null){
            roi.setStatus(OrderStatus.PREPARED.toString());
            restaurantOrderInformationRespository.save(roi);

            DeliveryInformationDTO diDTO=new DeliveryInformationDTO();
            diDTO.setOrderId(roi.getOrderId());
            diDTO.setStatus(roi.getStatus());
            diDTO.setDriverEmail(roi.getDriverEmail());
            diDTO.setPaymentStatus(roi.getPaymentStatus());

            System.out.println("###############");
            System.out.println(diDTO.getStatus());
            kafkaTemplatePrepared.send("payment.failed",diDTO);


            RestaurantOrderInformationEvent roiE=new RestaurantOrderInformationEvent();


            System.out.println("roi: "+roi.getStatus());
            roiE.setOrderId(roi.getOrderId());
            roiE.setCustomerId(roi.getCustomerId());
            roiE.setCustomerId(roi.getCustomerId());
            roiE.setStatus(roi.getStatus());
            roiE.setRestaurantId(roi.getRestaurantId());
            roiE.setAmount(roi.getAmount());

            CardDTO cardDTO=new CardDTO();
            cardDTO.setCardNumber(roi.getCard().getCardNumber());
            cardDTO.setCardType(roi.getCard().getCardType());
            cardDTO.setCvv(roi.getCard().getCvv());
            roiE.setCard(cardDTO);

            List<OrderItemDTO> dtoList = roi.getOrderItemList().stream().map(item -> {
                OrderItemDTO itemDto = new OrderItemDTO();
                itemDto.setMenuItemId(item.getMenuItemId());
                itemDto.setItemName(item.getItemName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPrice(item.getPrice());
                return itemDto;
            }).toList();
            roiE.setOrderItemList(dtoList);
            roiE.setPaymentStatus(roi.getPaymentStatus());
            System.out.println("******");
            System.out.println("roiE: "+roi.getStatus());


            System.out.println("###############");
            System.out.println("roiE: "+roiE.getStatus());

            kafkaTemplate.send("order.deliver",roiE);
            kafkaTemplate.send("order.notification",roiE);


        }

    }
}
