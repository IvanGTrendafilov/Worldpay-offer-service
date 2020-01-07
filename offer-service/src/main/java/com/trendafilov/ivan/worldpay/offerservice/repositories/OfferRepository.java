package com.trendafilov.ivan.worldpay.offerservice.repositories;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findOffersByMerchantMerchantIdAndStatus(Long merchantId, String status);

    List<Offer> findOffersByStudentStudentIdAndStatus(Long studentId, String status);

    @Query("select a from Offer a where a.expireDate <= :newDate and a.status='ACTIVE'")
    List<Offer> findAllActiveWithExpireDateBefore(
        @Param("newDate") Date newDate);

    List<Offer> findOffersByStudentStudentId(Long studentId);
}
