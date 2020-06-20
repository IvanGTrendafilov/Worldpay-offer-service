package com.trendafilov.ivan.worldpay.offerservice.dtos.requests;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentRequest {
    private String comment;
    private String userName;
    private Date dateCreated;
}
