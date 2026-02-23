package com.RestaurantEurekaServer.RestaurantEurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RestaurantEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantEurekaServerApplication.class, args);
	}

}
