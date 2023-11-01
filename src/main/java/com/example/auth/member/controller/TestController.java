package com.example.auth.member.controller;

import com.example.auth.member.entity.Member;
import com.example.auth.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository repository;
    
    // 로그인한 회원 찾기 테스트
    @GetMapping("/api/v1/test")
    public Optional<Member> test(@RequestHeader(value = "member-id") String header,
                                 @RequestHeader(value = "role") String header2){
        UUID memberId = UUID.fromString(header);
        System.out.println(header2);
        return repository.findById(memberId);
    }
}
