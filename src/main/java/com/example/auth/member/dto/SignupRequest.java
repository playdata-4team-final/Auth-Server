package com.example.auth.member.dto;

import com.example.auth.client.request.ProfessorRequest;
import com.example.auth.client.request.StudentRequest;
import com.example.auth.member.entity.Member;
import com.example.auth.member.entity.MemberRole;
import com.example.auth.member.entity.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    private String major;
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "전화번호를 입력해주세요.")
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
