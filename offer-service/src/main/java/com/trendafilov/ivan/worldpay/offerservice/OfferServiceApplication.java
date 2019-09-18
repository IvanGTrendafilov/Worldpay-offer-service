package com.trendafilov.ivan.worldpay.offerservice;

import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.Date;

@EnableScheduling
@SpringBootApplication
public class OfferServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(OfferServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(final OfferRepository offerRepository,
                             final MerchantRepository merchantRepository) {
        return args -> {
            final Merchant
                merchant =
                Merchant.builder()
                        .firstName("IVAN")
                        .lastName("TRENDAFILOV")
                        .build();
            merchantRepository.save(merchant);
            Arrays.asList("Description1, Description offer 2,description offer 3".split(","))
                  .forEach(x -> {

                      offerRepository.save(Offer.builder()
                                                .description(x)
                                                .merchant(merchant)
                                                .expireDate(new Date())
                                                .status(OfferStatus.ACTIVE.toString())
                                                .build());
                  });
            offerRepository.findAll()
                           .forEach(System.out::println);
        };
    }
}
