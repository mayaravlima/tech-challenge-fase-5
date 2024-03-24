package com.postech.fase5.ecommerceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EcommerceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceRegistryApplication.class, args);
    }

}
