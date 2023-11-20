package com.example.auth.member.dto;

import com.example.auth.client.request.MemberRequest;
import com.example.auth.client.request.ProfessorRequest;
import com.example.auth.client.request.StudentRequest;
import com.example.auth.member.entity.Member;
import com.example.auth.member.entity.MemberMajor;
import com.example.auth.member.entity.MemberRole;
import com.example.auth.member.entity.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<Long> majorIds;
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
                .majorIds(majorIds
                        .stream()
                        .map(id -> MemberMajor.builder().majorId(id).build())
                        .collect(Collectors.toList()))
                .build();
    }

    public StudentRequest toStudent(Member member,int studentNumber){
        return StudentRequest.builder()
                .id(member.getId())
                .studentName(member.getName())
                .year(1)
                .email(member.getEmail())
                .phNumber(member.getPhNumber())
                .studentNumber(studentNumber)
                .majorIds(member
                        .getMajorIds()
                        .stream()
                        .map(major -> major.getMajorId())
                        .collect(Collectors.toList()))
                .build();
    }

    public ProfessorRequest toProfessor(Member member){
        return ProfessorRequest.builder()
                .id(member.getId())
                .professorName(member.getName())
                .email(member.getEmail())
                .phNumber(member.getPhNumber())
                .majorIds(member
                        .getMajorIds()
                        .stream()
                        .map(major -> major.getMajorId())
                        .collect(Collectors.toList())
                )
                .build();
    }

    public MemberRequest toMember(Member member){
        return MemberRequest.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phNumber(member.getPhNumber())
                .majorIds(
                        member
                                .getMajorIds()
                                .stream()
                                .map(major -> major.getMajorId())
                                .collect(Collectors.toList())
                )
                .role(member.getRole())
                .status(member.getStatus())
                .build();
    }

}
