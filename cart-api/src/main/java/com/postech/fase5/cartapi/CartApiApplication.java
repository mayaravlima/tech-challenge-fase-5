package com.postech.fase5.cartapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartApiApplication.class, args);
	}

}
