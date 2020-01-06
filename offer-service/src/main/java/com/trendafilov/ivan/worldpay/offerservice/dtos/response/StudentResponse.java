package com.trendafilov.ivan.worldpay.offerservice.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {

    private Long studentId;
    private Long facultyNumber;
    private String firstName;
    private String lastName;
    private String specialty;
}
