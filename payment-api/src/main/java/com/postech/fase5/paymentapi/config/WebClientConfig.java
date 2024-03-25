package com.postech.fase5.paymentapi.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean("webclient")
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
}
