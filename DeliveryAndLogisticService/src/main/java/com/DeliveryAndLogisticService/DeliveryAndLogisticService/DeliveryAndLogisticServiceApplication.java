package com.DeliveryAndLogisticService.DeliveryAndLogisticService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliveryAndLogisticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryAndLogisticServiceApplication.class, args);
	}

}
