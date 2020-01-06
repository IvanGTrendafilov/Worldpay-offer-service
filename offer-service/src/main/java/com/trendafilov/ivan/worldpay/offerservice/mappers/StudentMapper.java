package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.StudentRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.StudentResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponse convertStudentEntityToResponse(final Student student) {
        if (student == null) {
            return StudentResponse.builder()
                                  .build();
        }
        return StudentResponse.builder()
                              .studentId(student.getStudentId())
                              .firstName(student.getFirstName())
                              .lastName(student.getLastName())
                              .facultyNumber(student.getFacultyNumber())
                              .specialty(student.getSpecialty())
                              .build();
    }

    public Student convertStudentRequestToEntity(final StudentRequest studentRequest) {
        return Student.builder()
                      .firstName(studentRequest.getFirstName())
                      .lastName(studentRequest.getLastName())
                      .facultyNumber(studentRequest.getFacultyNumber())
                      .specialty(studentRequest.getSpecialty())
                      .build();
    }
}
