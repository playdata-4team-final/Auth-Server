package com.example.auth.client.api;

import com.example.auth.client.request.ProfessorRequest;
import com.example.auth.client.request.StudentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("LMS-SERVER")
public interface LmsServerClient {

    @PostMapping("/api/v1/lms/student")
    ResponseEntity<Void> saveStudent(StudentRequest request);

    @PostMapping("/api/v1/lms/professor")
    ResponseEntity<Void> saveProfessor(ProfessorRequest request);

}
