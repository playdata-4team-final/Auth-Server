package com.example.auth.member.controller;

import com.example.auth.member.dto.EmailVerification;
import com.example.auth.member.dto.LoginRequest;
import com.example.auth.member.dto.SignupRequest;
import com.example.auth.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public void saveMember(@RequestBody SignupRequest request) {
        memberService.saveMember(request);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response){
        memberService.login(request,response);
    }

    @GetMapping("/reissue")
    public void reissue(@CookieValue(value = "RefreshToken", required = false) Cookie cookieRefreshToken, HttpServletResponse response){
        memberService.reissue(cookieRefreshToken,response);
    }

    @PostMapping("/send")
    public void emailVerification(@RequestBody EmailVerification emailVerification){
        memberService.emailVerification(emailVerification);
    }

}
