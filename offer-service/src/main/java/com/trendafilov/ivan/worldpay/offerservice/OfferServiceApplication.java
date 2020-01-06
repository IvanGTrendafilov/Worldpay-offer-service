package com.trendafilov.ivan.worldpay.offerservice;

import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.repositories.StudentRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.Date;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class OfferServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(OfferServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(final OfferRepository offerRepository,
                             final MerchantRepository merchantRepository,
                             final StudentRepository studentRepository) {
        return args -> {
            final Merchant
                merchant =
                Merchant.builder()
                        .firstName("Milen")
                        .lastName("Petrov")
                        .department("Software Technologies")
                        .build();
            merchantRepository.save(merchant);
            final Student
                student =
                Student.builder()
                       //.studentId(new Random().nextLong())
                       .facultyNumber(24657L)
                       .firstName("Ivan")
                       .lastName("Trendafilov")
                       .specialty("Software technologies")
                       .build();
            studentRepository.save(student);
            Arrays.asList("DIplomna rabota1, Diplomna offerta 2,Diplomna offerta 3".split(","))
                  .forEach(x -> {

                      Offer offer = Offer.builder()
                                         .description(x)
                                         .merchant(merchant)
                                         .student(student)
                                         .expireDate(new Date())
                                         .status(OfferStatus.ACTIVE.toString())
                                         .build();
                      offer = offerRepository.save(offer);
                  });
            offerRepository.findAll()
                           .forEach(System.out::println);
        };
    }
}
