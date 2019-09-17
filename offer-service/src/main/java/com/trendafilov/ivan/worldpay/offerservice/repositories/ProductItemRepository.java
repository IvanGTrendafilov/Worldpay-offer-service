package com.trendafilov.ivan.worldpay.offerservice.repositories;

import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

}
