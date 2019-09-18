package com.trendafilov.ivan.worldpay.offerservice.repositories;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findOffersByMerchantMerchantIdAndStatus(Long merchantId, String status);
}
