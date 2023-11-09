package com.example.auth.member.service;

import com.example.auth.client.api.LmsServerClient;
import com.example.auth.global.exception.NotFoundException;
import com.example.auth.global.util.JwtUtil;
import com.example.auth.member.dto.EmailVerification;
import com.example.auth.member.dto.LoginRequest;
import com.example.auth.member.dto.SignupRequest;
import com.example.auth.member.entity.Member;
import com.example.auth.member.entity.MemberRole;
import com.example.auth.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final LmsServerClient lmsServerClient;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void saveMember(SignupRequest request){
//        if (emailCheck(request.getEmail()) == request.getVerificationNumber()) {
            String encodePassword = passwordEncoder.encode(request.getPassword());
            Member member = repository.save(request.toEntity(encodePassword));
            if (request.getRole() == MemberRole.STUDENT) {
                ResponseEntity<Void> response = lmsServerClient.saveStudent(request.toStudent(member, 2023));
                if (response.getStatusCode() != HttpStatus.CREATED) {
                    String err = "-SERVICE DEAD";
                    throw new RuntimeException(err);
                }
                System.out.println("학생으로 회원가입");
            } else if (request.getRole() == MemberRole.PROFESSOR) {
                ResponseEntity<Void> response = lmsServerClient.saveProfessor(request.toProfessor(member));
                if (response.getStatusCode() != HttpStatus.CREATED) {
                    String err = "-SERVICE DEAD";
                    throw new RuntimeException(err);
                }
                System.out.println("교수로 회원가입");
            }
//        } else{
//            System.out.println("이메일 인증 안되어있음");
//        }
    }

    public void login(LoginRequest request, HttpServletResponse response) {

//        throw new NotFoundException("올바른 비밀번호가 아닙니다.");
        String userId = request.getUserId();
        String password = request.getPassword();

        // 회원 찾기 (userId)
        Member member = repository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("올바른 아이디가 아닙니다.")
        );

        // 회원 찾기 (password)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new NotFoundException("올바른 비밀번호가 아닙니다.");
        }

        String role = String.valueOf(member.getRole());

        System.out.println("로그인 성공");
        issueTokens(member.getId(),role,response);

    }

    public void issueTokens(UUID memberId, String role, HttpServletResponse response){
        String accessToken = jwtUtil.createAccessToken(memberId,role);
        String refreshToken = jwtUtil.createRefreshToken(memberId,role);
        jwtUtil.addJwtToCookie("AccessToken", accessToken, response);
        jwtUtil.addJwtToCookie("RefreshToken", refreshToken, response);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(String.valueOf(memberId),refreshToken);
    }

    public void reissue(Cookie cookieRefreshToken,HttpServletResponse response) {

        // 쿠키에 담긴 리프레시 토큰 가져와서 멤버 아이디 조회
        // 쿠키 없을시 예외처리
        String refreshTokenValue = cookieRefreshToken.getValue();
        UUID memberId = jwtUtil.getMemberIdFromToken(refreshTokenValue);

        // 멤버 아이디로 redis 에 저장되어 있는 토큰 가져오기
        String redisRefreshToken = redisTemplate.opsForValue().get(String.valueOf(memberId)).substring(7);

        // 유효성 검사
        String msg = jwtUtil.validateToken(redisRefreshToken);

        // 토큰값 비교
        if (refreshTokenValue.equals(redisRefreshToken)) {
            System.out.println("올바른 토큰임");

            // 액세스 토큰 재발급
            String role = jwtUtil.getRoleFromToken(redisRefreshToken);
            String accessToken = jwtUtil.createAccessToken(memberId,role);
            jwtUtil.addJwtToCookie("AccessToken", accessToken, response);

        } else{
            System.out.println("토큰 탈취 우려있음");
            System.out.println("1."+refreshTokenValue);
            System.out.println("2."+redisRefreshToken.substring(7));

            // 리프레시 토큰 삭제
            redisTemplate.delete(String.valueOf(memberId));

            // 로그인 다시 시키기
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }

    private final long EXPIRATION_SECONDS = 60; // 1 min

    @Transactional
    public void emailVerification(EmailVerification emailVerification) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailVerification.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("이메일 인증번호 입니다."); // 메일 제목
            mimeMessageHelper.setText("1648", false); // 메일 본문 내용, HTML 여부
            // 잠시 메일보내기 막음
//             javaMailSender.send(mimeMessage);

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(emailVerification.getEmail(),"1648",EXPIRATION_SECONDS, TimeUnit.SECONDS);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean emailCheck(EmailVerification emailVerification) {
        String redisVerification = Optional.ofNullable(
                redisTemplate.opsForValue().get(emailVerification.getEmail())
        ).orElseThrow( () -> new NotFoundException("이메일 인증하기를 눌러주세요.") );
        return redisVerification.equals(emailVerification.getVerificationNumber());
    }
    
}
