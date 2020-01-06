package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.StudentRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.StudentResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;

import java.util.List;

public interface IStudentService {

    List<StudentResponse> getAllStudents();

    Student findStudentByStudentId(String studentId);

    StudentResponse getStudentResponseByStudentEntity(Student student);

    StudentResponse createStudentByStudentRequest(StudentRequest studentRequest);
}
