package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.LoginRequest;
import msa.userservice.adapter.in.web.dto.LoginResponse;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.adapter.out.persistence.MemberRoleRepository;
import msa.userservice.application.port.in.LoginUseCase;
import msa.userservice.domain.Member;
import msa.userservice.domain.MemberRole;
import msa.userservice.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRoleRepository memberRoleRepository;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Member member = memberRepository.findByLoginId(loginRequest.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));

            if(member.getStatusCode() != 1) {
                throw new IllegalArgumentException("활성화되지 않은 계정입니다.");
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            // 실제 Role(권한) 정보 조회
            List<MemberRole> memberRoles = memberRoleRepository.findByMember(member);
            List<String> roleNames = memberRoles.stream()
                    .map(mr -> mr.getRole().getRoleName())
                    .collect(Collectors.toList());
            String roles = String.join(",", roleNames); // ex) "ADMIN,USER"

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