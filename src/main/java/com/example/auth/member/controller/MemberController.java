package com.example.auth.member.controller;

import com.example.auth.global.response.LmsResponse;
import com.example.auth.member.dto.EmailVerification;
import com.example.auth.member.dto.LoginRequest;
import com.example.auth.member.dto.SignupRequest;
import com.example.auth.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public LmsResponse<String> saveMember(@RequestBody @Valid SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new LmsResponse<>(HttpStatus.BAD_REQUEST, "", "유효성 검사 실패", errorMessage, LocalDateTime.now());
        }
        memberService.saveMember(request);
        return new LmsResponse<>(HttpStatus.OK, "", "로그인 성공", "", LocalDateTime.now());
    }

    @PostMapping("/login")
    public LmsResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        memberService.login(request,response);
        return new LmsResponse<>(HttpStatus.OK, "", "조회 성공", "", LocalDateTime.now());
    }

    @GetMapping("/reissue")
    public void reissue(@CookieValue(value = "RefreshToken", required = false) Cookie cookieRefreshToken, HttpServletResponse response){
        memberService.reissue(cookieRefreshToken,response);
    }

    @PostMapping("/send")
    public void emailVerification(@RequestBody EmailVerification emailVerification){
        memberService.emailVerification(emailVerification);
    }

    @PostMapping("/check")
    public LmsResponse<String> emailCheck(@RequestBody EmailVerification emailVerification){
        if(memberService.emailCheck(emailVerification)){
            return new LmsResponse<>(HttpStatus.OK, "true", "인증 성공", "", LocalDateTime.now());
        } else {
            return new LmsResponse<>(HttpStatus.OK, "false", "인증 실패", "올바른 인증번호가 아닙니다.", LocalDateTime.now());
        }
    }

}
