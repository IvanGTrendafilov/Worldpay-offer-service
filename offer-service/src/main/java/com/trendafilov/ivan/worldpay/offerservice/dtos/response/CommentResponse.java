package com.trendafilov.ivan.worldpay.offerservice.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String userName;
    private Date dateCreated;
    private Long offerId;
}
