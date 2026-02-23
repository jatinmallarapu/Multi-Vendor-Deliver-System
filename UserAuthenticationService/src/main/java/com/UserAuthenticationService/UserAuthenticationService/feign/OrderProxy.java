package com.UserAuthenticationService.UserAuthenticationService.feign;

import com.UserAuthenticationService.UserAuthenticationService.dto.CreateOrderRequest;
import com.UserAuthenticationService.UserAuthenticationService.dto.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ORDERMANAGEMENTSERVICE")
public interface OrderProxy {

    @PostMapping("/orders")
    public ResponseEntity<Object> placeOrder(@RequestBody CreateOrderRequest request);

    @GetMapping("/orders/{customerId}")
    public ResponseEntity<String> getOrder(@PathVariable("customerId") Long customerId);

    @GetMapping("/orders/allorder/{customerid}")
    public List<Order> getAllCustomerOrders(@PathVariable("customerid")Long customerid);

    @GetMapping("/orders/cancel/{orderid}")
    public Order cancelOrder(@PathVariable("orderid") Long orderid);

    @GetMapping("/orders/ratedriver/{orderid}")
    public ResponseEntity<String> rateDriver(@PathVariable("orderid") Long orderid,
            @RequestParam("rating") Double rating);


}
