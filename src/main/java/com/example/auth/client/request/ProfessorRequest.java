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
public class ProfessorRequest {
    private UUID id;
    private String professorName;
    private String email;
    private String phNumber;
}
