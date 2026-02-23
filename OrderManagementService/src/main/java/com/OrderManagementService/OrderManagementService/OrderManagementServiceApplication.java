package com.OrderManagementService.OrderManagementService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableFeignClients
public class OrderManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementServiceApplication.class, args);
	}

}
