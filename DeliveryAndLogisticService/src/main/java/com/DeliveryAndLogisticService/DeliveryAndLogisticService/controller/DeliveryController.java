package com.DeliveryAndLogisticService.DeliveryAndLogisticService.controller;

import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.DeliveryInformationDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.dto.DriverDTO;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.DeliveryInformation;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.entity.Driver;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DeliveryInformationRepository;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.repository.DriverRepository;
import com.DeliveryAndLogisticService.DeliveryAndLogisticService.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryInformationRepository deliveryInformationRepository;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DriverRepository driverRepository;

    @PostMapping("/addDriverInfo")
    public String addDriverInfo(@RequestBody DriverDTO driverInfo){
        Driver driver=new Driver();
        driver.setEmail(driverInfo.getEmail());
        driver.setAvailable(driverInfo.isAvailable());
        driver.setRating(driverInfo.getRating());
        driverRepository.save(driver);
        return "Driver registered";
    }

    @GetMapping("/allorders")
    public List<DeliveryInformation> getAllOrders(){
        return deliveryInformationRepository.findAll();
    }

    @GetMapping("/orderdelivering/{orderid}")
    public DeliveryInformationDTO updateOrderAndInformUser(@PathVariable("orderid") Long orderid){
        return deliveryService.updateOrderAndInformUser(orderid, null);
    }

    @GetMapping("/updatedeliveredorder/{orderid}")
    public DeliveryInformationDTO updateOrderStatusToDeliveredAndInformToUser(@PathVariable("orderid") Long orderid){
        return deliveryService.updateOrderStatusToDeliveredAndInformToUser(orderid);
    }

    @GetMapping("/deliverycancel/{orderid}")
    public String cancelDelivery(@PathVariable("orderid") Long orderid){
        return deliveryService.cancelOrderDelivery(orderid);

    }

}
