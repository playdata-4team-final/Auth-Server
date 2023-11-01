package com.example.auth.member.dto;

import com.example.auth.client.request.ProfessorRequest;
import com.example.auth.client.request.StudentRequest;
import com.example.auth.member.entity.Member;
import com.example.auth.member.entity.MemberRole;
import com.example.auth.member.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String userId;
    private String password;
    private String name;
    private String major;
    private String email;
    private String phNumber;
    private String verificationNumber;
    private MemberStatus status;
    private MemberRole role;

    public Member toEntity(String encodePassword){
        return Member.builder()
                .userId(userId)
                .password(encodePassword)
                .name(name)
                .email(email)
                .phNumber(phNumber)
                .status(status)
                .role(role)
                .build();
    }

    public StudentRequest toStudent(Member member,int studentNumber){
        return StudentRequest.builder()
                .id(member.getId())
                .studentName(name)
                .year(1)
                .email(email)
                .phNumber(phNumber)
                .studentNumber(studentNumber)
                .build();
    }

    public ProfessorRequest toProfessor(Member member){
        return ProfessorRequest.builder()
                .id(member.getId())
                .professorName(name)
                .email(email)
                .phNumber(phNumber)
                .build();
    }

}
