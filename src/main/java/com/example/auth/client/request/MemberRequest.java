package com.example.auth.client.request;


import com.example.auth.member.entity.MemberRole;
import com.example.auth.member.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {
    private UUID id;
    private String name;
    private Integer year;
    private String email;
    private String phNumber;
    private MemberRole role;
    private List<Long> majorIds;
    private MemberStatus status;
}
