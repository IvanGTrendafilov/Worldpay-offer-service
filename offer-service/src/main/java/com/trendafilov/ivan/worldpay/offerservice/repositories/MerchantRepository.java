package com.trendafilov.ivan.worldpay.offerservice.repositories;

import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

}
