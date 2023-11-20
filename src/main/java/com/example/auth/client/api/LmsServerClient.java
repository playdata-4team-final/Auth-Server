package com.example.auth.client.api;

import com.example.auth.client.request.ProfessorRequest;
import com.example.auth.client.request.StudentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("LMS-SERVER")
public interface LmsServerClient {

    @PostMapping("/api/v1/lms/student")
    ResponseEntity<Void> saveStudent(@RequestBody StudentRequest request);

    @PostMapping("/api/v1/lms/professor")
    ResponseEntity<Void> saveProfessor(@RequestBody ProfessorRequest request);
}
