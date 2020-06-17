package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.StudentRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.StudentResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.StudentMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.StudentRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IStudentService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentResponse> getAllStudents() {
        final List<Student> studentList = studentRepository.findAll();
        return studentList.stream()
                          .map(student -> getStudentResponseByStudentEntity(student))
                          .collect(
                              Collectors.toList());
    }

    @Override
    public Student findStudentByStudentId(final String studentId) {
        long provideдStudentId = 0L;
        Student student = null;
        try {
            provideдStudentId = Long.parseLong(studentId);
            final Optional<Student>
                studentOptional =
                studentRepository.findById(provideдStudentId);
            if (!studentOptional.isPresent()) {
                log.error("findStudentByStudentId for studentId : {} was not found",
                          studentId);
                return throwOfferServiceException(
                    ErrorMessagesEnum.STUDENT_NOT_FOUND.getMessage());
            }
            student = studentOptional.get();
        } catch (final NumberFormatException e) {
            log.error("Random string {} is provided during findStudentByStudentId", studentId);
            throwOfferServiceException(ErrorMessagesEnum.STUDENT_NOT_FOUND.getMessage());
        }
        return student;
    }

    private Student throwOfferServiceException(final String message) throws OfferServiceException {
        throw new OfferServiceException(message,
                                        HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public StudentResponse getStudentResponseByStudentEntity(final Student student) {
        return studentMapper.convertStudentEntityToResponse(student);
    }

    @Override
    public StudentResponse createStudentByStudentRequest(final StudentRequest studentRequest) {
        if (StringUtils.isEmpty(studentRequest.getFirstName())) {
            log.error(
                "Student first name is not provided for createStudentByStudentRequest with StudentRequest: {}",
                studentRequest);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_NAME_EMPTY.getMessage());
        }
        if (studentRequest.getFacultyNumber() == null || studentRequest.getFacultyNumber() == 0) {
            log.error(
                "Merchant department is not provided for createStudentByStudentRequest with StudentRequest: {}",
                studentRequest);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_DEPARTMENT_EMPTY.getMessage());
        }
        Student student = studentMapper.convertStudentRequestToEntity(studentRequest);
        student = studentRepository.save(student);
        return studentMapper.convertStudentEntityToResponse(student);
    }
}
