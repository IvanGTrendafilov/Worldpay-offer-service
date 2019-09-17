package com.trendafilov.ivan.worldpay.offerservice.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Offer {

    @Id
    @GeneratedValue
    private Long offerId;
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchantId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "merchantId")
    @JsonIdentityReference(alwaysAsId = true)
    private Merchant merchant;

    @Override
    public String toString() {
        return "Offer{" +
               "offerId=" + offerId +
               ", description='" + description + '\'' +
               ", merchant=" + merchant.getMerchantId() +
               '}';
    }
}
