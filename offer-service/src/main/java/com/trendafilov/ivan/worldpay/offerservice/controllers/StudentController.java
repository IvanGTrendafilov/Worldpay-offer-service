package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.StudentRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.StudentResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;
import com.trendafilov.ivan.worldpay.offerservice.mappers.StudentMapper;
import com.trendafilov.ivan.worldpay.offerservice.services.IStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "Student Controller")
@RestController
@RequestMapping("/student/v1")
public class StudentController {

    private final IStudentService studentService;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentController(
        final IStudentService studentService,
        final StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @ApiOperation(
        value = "Get all Students",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Get all Students",
        response = StudentResponse.class)
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllStudents() {
        log.info("Inside get All Students endpoint");
        final List<StudentResponse> allStudents = studentService.getAllStudents();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Get Student by Id ",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Get Student by id. OfferServiceException is thrown when Student is invalid",
        response = StudentResponse.class)
    @GetMapping(value = "/{studentId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStudentById(@PathVariable final String studentId) {
        log.info("Inside get Student by id endpoint with Student id: {}", studentId);
        final Student Student = studentService.findStudentByStudentId(studentId);
        return new ResponseEntity<>(studentMapper.convertStudentEntityToResponse(Student),
                                    HttpStatus.OK);
    }

    @ApiOperation(
        value = "Insert Student",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Insert Student",
        response = StudentResponse.class)
    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveStudent(
        @Valid @RequestBody final StudentRequest studentRequest) {
        log.info("Inside save merchant endpoint with Request body: {}", studentRequest);
        final StudentResponse
            studentResponse =
            studentService.createStudentByStudentRequest(studentRequest);
        log.info("Save student endpoint return Response body: {}", studentResponse);
        return new ResponseEntity<>(studentResponse, HttpStatus.CREATED);
    }
}
