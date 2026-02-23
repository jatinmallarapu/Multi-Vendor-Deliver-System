package com.UserAuthenticationService.UserAuthenticationService.feign;

import com.UserAuthenticationService.UserAuthenticationService.dto.DeliveryInformation;
import com.UserAuthenticationService.UserAuthenticationService.dto.DeliveryInformationDTO;
import com.UserAuthenticationService.UserAuthenticationService.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "DELIVERY-SERVICE") // Assuming port 8084
public interface DeliveryProxy {

    @PostMapping("/delivery/addDriverInfo")
    public String addDriverInfo(@RequestBody DriverDTO driverInfo);

    @GetMapping("/delivery/allorders")
    public List<DeliveryInformation> getAllOrders();

    @GetMapping("/delivery/orderdelivering/{orderid}")
    public DeliveryInformationDTO updateOrderAndInformUser(@PathVariable("orderid") Long orderid);

    @GetMapping("/delivery/updatedeliveredorder/{orderid}")
    public DeliveryInformationDTO updateOrderStatusToDeliveredAndInformToUser(@PathVariable("orderid") Long orderid);

    @GetMapping("/delivery/deliverycancel/{orderid}")
    public String cancelDelivery(@PathVariable("orderid") Long orderid);
}
