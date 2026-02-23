package com.OrderManagementService.OrderManagementService.controller;

import com.OrderManagementService.OrderManagementService.dto.CreateOrderRequest;
import com.OrderManagementService.OrderManagementService.entity.Order;
import com.OrderManagementService.OrderManagementService.event.PaymentStatusEvent;
import com.OrderManagementService.OrderManagementService.repository.OrderRepository;
import com.OrderManagementService.OrderManagementService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    // @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> placeOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getOrder(@PathVariable("customerId") Long customerId) {
        Order order = orderRepository.findTopByCustomerIdOrderByCreatedAtDesc(customerId).get();
        return ResponseEntity.ok(order.getStatus().toString());

    }

    @GetMapping("/cancel/{orderid}")
    public Order cancelOrder(@PathVariable("orderid") Long orderid) {
        return orderService.cancelOrder(orderid);
    }

    @GetMapping("/ratedriver/{orderid}")
    public ResponseEntity<String> rateDriver(@PathVariable("orderid") Long orderid,
            @RequestParam("rating") Double rating) {
        orderService.rateDriver(orderid, rating);
        return ResponseEntity.ok("Driver rated successfully");

    }

    @GetMapping("/allorder/{customerid}")
    public List<Order> getAllCustomerOrders(@PathVariable("customerid")Long customerid){
        return orderRepository.findAllByCustomerId(customerid);
    }

}
