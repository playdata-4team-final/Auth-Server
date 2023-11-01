package com.example.auth.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 1000L * 24 * 7; // 1주일

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 액세스 토큰 생성
    public String createAccessToken(UUID memberId, String role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(memberId)) // 사용자 식별자값(ID)
                        .claim("role", role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    
    //리프레시 토큰 생성
    public String createRefreshToken(UUID memberId, String role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(memberId)) // 사용자 식별자값(ID)
                        .claim("role", role)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    
    // JWT Cookie 에 저장
    public void addJwtToCookie(String tokenType,String token, HttpServletResponse response) {

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(tokenType, token.substring(7));

        // 브라우저에서 쿠키 접근 방지
        cookie.setHttpOnly(true);

        // HTTPS 연결에서만 쿠키 전송
        // cookie.setSecure(true);

        // 쿠키 유효기간 설정
        if (tokenType == "AccessToken") {
            cookie.setMaxAge((int) ACCESS_TOKEN_TIME);
        } else {
            cookie.setMaxAge((int) REFRESH_TOKEN_TIME);
        }

        cookie.setPath("/");

        // Response 객체에 Cookie 추가
        response.addCookie(cookie);
    }
    
    // 토큰으로 유저찾기
    public UUID getMemberIdFromToken(String token) {
//        String tokenValue = token.substring(7);
//        System.out.println(token);
//        System.out.println(tokenValue);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public String getRoleFromToken(String token) {
//        String tokenValue = token.substring(7);
//        System.out.println(token);
//        System.out.println(tokenValue);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("role");
    }

    public String validateToken(String token) {
        String tokenValue = token.substring(7);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenValue);
            return "Authorized";
        } catch (SecurityException | MalformedJwtException e) {
            // 유효하지 않는 JWT 서명
            return "Unauthorized: Invalid JWT signature";
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰
            return "Unauthorized: Expired JWT token";
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰
            return "Unauthorized: Unsupported JWT token";
        } catch (IllegalArgumentException e) {
            // 잘못된 JWT 토큰
            return "Unauthorized: JWT claims is empty";
        }
    }
}