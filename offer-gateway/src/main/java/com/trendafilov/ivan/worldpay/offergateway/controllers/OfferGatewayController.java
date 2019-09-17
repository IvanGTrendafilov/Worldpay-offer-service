package com.trendafilov.ivan.worldpay.offergateway.controllers;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.trendafilov.ivan.worldpay.offergateway.OfferClient;
import com.trendafilov.ivan.worldpay.offergateway.dtos.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/offers")
public class OfferGatewayController {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    private OfferClient offerClient;

    @GetMapping("/all")
    public Collection<Offer> getOfferDescriptions() {
        final ParameterizedTypeReference<Resources<Offer>> resourcesParameterizedTypeReference = new ParameterizedTypeReference<Resources<Offer>>() {
        };
        final ResponseEntity<Resources<Offer>>
            resposneEntity = this.restTemplate.exchange("http://OFFER-SERVICE:8025/offer/v1", HttpMethod.GET, null, resourcesParameterizedTypeReference);
        return resposneEntity.getBody().getContent();
      //  return offerClient.findAllOffersInStore();
    }
}
