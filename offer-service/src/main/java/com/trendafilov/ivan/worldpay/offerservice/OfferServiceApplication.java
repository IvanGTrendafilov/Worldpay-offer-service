package com.trendafilov.ivan.worldpay.offerservice;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class OfferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfferServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner runner(final OfferRepository offerRepository) {
		return args -> {
			Arrays.asList("Description1, Description offer 2,description offer 3".split(","))
				  .forEach(x -> offerRepository.save(Offer.builder().description(x).build()));
			offerRepository.findAll().forEach(System.out::println);
		};
	}
}
