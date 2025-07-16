package msa.userservice.application.service;

import msa.userservice.adapter.in.web.LoginRequest;
import msa.userservice.adapter.in.web.LoginResponse;
import msa.userservice.adapter.out.persistence.jpa.MemberRepository;
import msa.userservice.application.port.in.LoginUseCase;
import msa.userservice.domain.Member;
import msa.userservice.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class LoginService implements LoginUseCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Member member = memberRepository.findByLoginId(loginRequest.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));

            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            // TODO: MemberRole을 기반으로 권한(roles) 생성
            String roles = "USER"; // 임시로 USER 권한 부여

            String accessToken = jwtTokenProvider.createToken(member.getLoginId(), roles);
            String refreshToken = jwtTokenProvider.createToken(member.getLoginId(), roles);

            // Redis에 리프레시 토큰 저장 (만료 시간 설정)
            redisTemplate.opsForValue().set(
                    member.getLoginId(),
                    refreshToken,
                    jwtTokenProvider.getRefreshTokenValidityInMilliseconds(),
                    TimeUnit.MILLISECONDS
            );

            return new LoginResponse(accessToken, refreshToken);
        } catch (IllegalArgumentException e) {
            logger.error("Login failed: {}", e.getMessage());
            throw e; // Re-throw the exception so it can be handled by Spring's exception handling
        }
    }
}