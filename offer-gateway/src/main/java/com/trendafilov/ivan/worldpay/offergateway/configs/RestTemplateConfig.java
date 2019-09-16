package com.trendafilov.ivan.worldpay.offergateway.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(
        final RestTemplateBuilder restTemplateBuilder) {
        final RestTemplate
            restTemplate =
                              restTemplateBuilder.build();
        return restTemplate;
    }
}
