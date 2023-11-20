package com.example.auth.client.api;

import com.example.auth.client.request.MemberRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("MAIL-SERVER")
public interface MailServerClient {

    @PostMapping("/api/v1/member")
    ResponseEntity<Void> saveMember(@RequestBody MemberRequest request);

}
