package com.example.auth.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
    private UUID id;
    private String studentName;
    private Integer year;
    private int studentNumber;
    private String email;
    private String phNumber;
}
