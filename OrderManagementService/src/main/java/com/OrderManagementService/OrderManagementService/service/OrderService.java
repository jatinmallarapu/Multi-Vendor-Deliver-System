package com.OrderManagementService.OrderManagementService.service;

import com.OrderManagementService.OrderManagementService.dto.CardDTO;
import com.OrderManagementService.OrderManagementService.dto.CreateOrderRequest;
import com.OrderManagementService.OrderManagementService.dto.OrderItemDTO;
import com.OrderManagementService.OrderManagementService.dto.RestaurantItem;
import com.OrderManagementService.OrderManagementService.entity.Order;
import com.OrderManagementService.OrderManagementService.entity.OrderItem;
import com.OrderManagementService.OrderManagementService.entity.OrderStatus;
import com.OrderManagementService.OrderManagementService.event.OrderEvent;
import com.OrderManagementService.OrderManagementService.event.RestaurantOrderInformationEvent;
import com.OrderManagementService.OrderManagementService.exception.ItemNotFoundException;
import com.OrderManagementService.OrderManagementService.exception.RestaurantNotFoundException;
import com.OrderManagementService.OrderManagementService.feign.RestaurantProxy;
import com.OrderManagementService.OrderManagementService.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    RestaurantProxy restaurantProxy;

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, RestaurantOrderInformationEvent> kafkaTemplateRoie;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderEvent> kafkaTemplate)
            throws Exception {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private String topic = "order.created";

    @Transactional
    public Order placeOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());

        String restauarantExistsOrNot = checkIfRestaurantExistsWithCircuitBreaker(request.getRestaurantId());

        if (restauarantExistsOrNot.equals("Restaurant Not Found")) {
            throw new RestaurantNotFoundException("Restaurant Not Found");
        } else if (restauarantExistsOrNot.equals("Service Unavailable")) {
            throw new RuntimeException("Restaurant Service is temporarily unavailable. Please try again later.");
        }
        order.setRestaurantId(request.getRestaurantId());
        order.setStatus(OrderStatus.CREATED);
        order.setCard(request.getCard());

        List<OrderItem> items = request.getItems().parallelStream().map(i -> {

            OrderItem item = new OrderItem();

            RestaurantItem restaurantItem = checkIfRestaurantItemExistsWithCircuitBreaker(request.getRestaurantId(),
                    i.getMenuItemId());
            if (restaurantItem == null) {
                throw new ItemNotFoundException("Item Not Found");
            }
            if (restaurantItem.getMenuItemId() == -1L) {
                throw new RuntimeException("Restaurant Service is temporarily unavailable. Please try again later.");
            }
            item.setMenuItemId(i.getMenuItemId());
            item.setItemName(i.getItemName());
            item.setQuantity(i.getQuantity());
            item.setPrice(restaurantItem.getPrice());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        CardDTO dto = new CardDTO();
        dto.setCardType(saved.getCard().getCardType().toString());
        dto.setCardNumber(saved.getCard().getCardno());
        dto.setCvv(saved.getCard().getCvv());

        List<OrderItemDTO> dtoList = saved.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setItemName(item.getItemName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();

        RestaurantOrderInformationEvent roiE = new RestaurantOrderInformationEvent();
        roiE.setOrderId(saved.getId());
        roiE.setCustomerId(saved.getCustomerId());
        roiE.setCustomerId(saved.getCustomerId());
        roiE.setStatus(saved.getStatus().toString());
        roiE.setRestaurantId(saved.getRestaurantId());
        roiE.setAmount(saved.getTotalAmount());

        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardNumber(saved.getCard().getCardno());
        cardDTO.setCardType(saved.getCard().getCardType().toString());
        cardDTO.setCvv(saved.getCard().getCvv());
        roiE.setCard(cardDTO);

        List<OrderItemDTO> roiEdtoList = saved.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setItemName(item.getItemName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();
        roiE.setOrderItemList(roiEdtoList);
        roiE.setPaymentStatus("PAYMENT PROCESSING");
        roiE.setDriverEmail(saved.getDriverEmail());

        kafkaTemplateRoie.send("order.notification", roiE);
        kafkaTemplate.send(
                topic,
                new OrderEvent(
                        saved.getId(),
                        saved.getStatus(),
                        saved.getCustomerId(),
                        saved.getRestaurantId(),
                        saved.getTotalAmount(),
                        dto,
                        dtoList));
        System.out.println(order.toString());
        return saved;
    }

    @Transactional
    public Order cancelOrder(Long orderid) {
        Order order = orderRepository.findById(orderid).get();
        if (order.getStatus().toString().equals("PREPARED") || order.getStatus().toString().equals("DELIVERING")
                || order.getStatus().toString().equals("DELIVERED")) {
            throw new IllegalStateException(
                    "Order cannot be cancelled because it is currently in status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        CardDTO dto = new CardDTO();
        dto.setCardType(saved.getCard().getCardType().toString());
        dto.setCardNumber(saved.getCard().getCardno());
        dto.setCvv(saved.getCard().getCvv());

        List<OrderItemDTO> dtoList = saved.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();

        kafkaTemplate.send(
                topic,
                new OrderEvent(
                        saved.getId(),
                        saved.getStatus(),
                        saved.getCustomerId(),
                        saved.getRestaurantId(),
                        saved.getTotalAmount(),
                        dto,
                        dtoList));

        // FOR ORDER.NOTIFICATION
        RestaurantOrderInformationEvent roiE = new RestaurantOrderInformationEvent();
        roiE.setOrderId(saved.getId());
        roiE.setCustomerId(saved.getCustomerId());
        roiE.setCustomerId(saved.getCustomerId());
        roiE.setStatus(saved.getStatus().toString());
        roiE.setRestaurantId(saved.getRestaurantId());
        roiE.setAmount(saved.getTotalAmount());

        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardNumber(saved.getCard().getCardno());
        cardDTO.setCardType(saved.getCard().getCardType().toString());
        cardDTO.setCvv(saved.getCard().getCvv());
        roiE.setCard(cardDTO);

        List<OrderItemDTO> roiEdtoList = saved.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();
        roiE.setOrderItemList(roiEdtoList);
        roiE.setPaymentStatus("CANCELLED");
        roiE.setDriverEmail(saved.getDriverEmail());

        kafkaTemplateRoie.send("order.notification", roiE);
        return saved;
    }

    @Transactional(readOnly = false)
    public void rateDriver(Long orderId, Double rating) {
        Order order = orderRepository.findById(orderId).get();
        if (order != null) {
            RestaurantOrderInformationEvent roiE = new RestaurantOrderInformationEvent();
            roiE.setOrderId(orderId);
            roiE.setRating(rating);
            kafkaTemplateRoie.send("order.deliver", roiE);
        }
    }

    @CircuitBreaker(name = "RESTAURANTSERVICE", fallbackMethod = "checkIfRestaurantExistsFallback")
    private String checkIfRestaurantExistsWithCircuitBreaker(Long restaurantId) {
        return restaurantProxy.checkIfRestaurantExists(restaurantId);
    }

    public String checkIfRestaurantExistsFallback(Long restaurantId, Throwable t) {
        System.out.println("Fallback for checkIfRestaurantExists called due to: " + t.getMessage());
        return "Service Unavailable";
    }

    @CircuitBreaker(name = "RESTAURANTSERVICE", fallbackMethod = "checkIfRestaurantItemExistsFallback")
    private RestaurantItem checkIfRestaurantItemExistsWithCircuitBreaker(Long restaurantId, Long itemId) {
        return restaurantProxy.checkIfRestaurantItemExists(restaurantId, itemId);
    }

    public RestaurantItem checkIfRestaurantItemExistsFallback(Long restaurantId, Long itemId, Throwable t) {
        System.out.println("Fallback for checkIfRestaurantItemExists called due to: " + t.getMessage());
        RestaurantItem fallbackItem = new RestaurantItem();
        fallbackItem.setMenuItemId(-1L); // Use menuItemId -1 as a flag
        fallbackItem.setPrice(BigDecimal.ZERO);
        return fallbackItem;
    }
}
