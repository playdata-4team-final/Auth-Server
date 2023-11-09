package com.example.auth.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String email;

    private String phNumber;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

}
