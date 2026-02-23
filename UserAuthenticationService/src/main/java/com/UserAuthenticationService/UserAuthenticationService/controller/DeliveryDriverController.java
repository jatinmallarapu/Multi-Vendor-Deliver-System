package com.UserAuthenticationService.UserAuthenticationService.controller;

import com.UserAuthenticationService.UserAuthenticationService.dto.DeliveryInformation;
import com.UserAuthenticationService.UserAuthenticationService.dto.DeliveryInformationDTO;
import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.feign.DeliveryProxy;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DeliveryDriverController {

    @Autowired
    private DeliveryProxy deliveryProxy;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/getuserid/{useremail}")
    public Long getUserId(@PathVariable("useremail") String useremail){
        User userdb = userRepository.findByEmail(useremail);
        return userdb.getId();
    }

    @GetMapping("/allorders")
    public List<DeliveryInformation> getAllOrders() {
        return deliveryProxy.getAllOrders();
    }


    @GetMapping("/orderdelivering/{orderid}")
    public DeliveryInformationDTO updateOrderAndInformUser(@PathVariable("orderid") Long orderid){
        return deliveryProxy.updateOrderAndInformUser(orderid);
    }

    @GetMapping("/updatedeliveredorder/{orderid}")
    public DeliveryInformationDTO updateOrderStatusToDeliveredAndInformToUser(@PathVariable("orderid") Long orderid) {
        return deliveryProxy.updateOrderStatusToDeliveredAndInformToUser(orderid);
    }

    @GetMapping("/deliverycancel/{orderid}")
    public String cancelDelivery(@PathVariable("orderid") Long orderid) {
        return deliveryProxy.cancelDelivery(orderid);
    }
}
