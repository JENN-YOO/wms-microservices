package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.LoginRequest;
import msa.userservice.adapter.in.web.dto.LoginResponse;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.adapter.out.persistence.MemberRoleRepository;
import msa.userservice.application.port.in.LoginUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.application.port.out.MemberRolePersistencePort;
import msa.userservice.config.MDCHelper;
import msa.userservice.domain.Member;
import msa.userservice.domain.MemberRole;
import msa.userservice.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    //TODO: MemberRoleRepository를 사용하지 않고, MemberRolePersistencePort를 사용하도록 변경
    private final MemberPersistencePort memberPersistencePort;
    private final MemberRolePersistencePort memberRolePersistencePort;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        MDC.put("loginId", loginRequest.getUsername());
        MDCHelper.appendDebug(LoginService.class, "로그인 처리 시작"); // <-- 1. 시작 기록

        try {
            Member member = memberRepository.findByLoginId(loginRequest.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
            MDCHelper.appendDebug(LoginService.class, "회원 정보 조회 성공: " + member.getLoginId()); // <-- 2. 회원 조회 성공 기록

            if(member.getStatusCode() != 1) {
                MDCHelper.appendDebug(LoginService.class, "계정 비활성화 상태. StatusCode: " + member.getStatusCode()); // <-- 3. 상태 체크 기록
                throw new IllegalArgumentException("활성화되지 않은 계정입니다.");
            }
            MDCHelper.appendDebug(LoginService.class, "계정 활성화 상태 확인 (정상)"); // <-- 3. 상태 체크 기록

            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                MDCHelper.appendDebug(LoginService.class, "비밀번호 불일치"); // <-- 4. 비밀번호 체크 기록
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }
            MDCHelper.appendDebug(LoginService.class, "비밀번호 일치 확인"); // <-- 4. 비밀번호 체크 기록

            // 실제 Role(권한) 정보 조회
            List<MemberRole> memberRoles = memberRoleRepository.findByMember(member);
            List<String> roleNames = memberRoles.stream()
                    .map(mr -> mr.getRole().getRoleName())
                    .collect(Collectors.toList());
            String roles = String.join(",", roleNames); // ex) "ADMIN,USER"
            MDCHelper.appendDebug(LoginService.class, "사용자 권한 조회 완료: " + roles); // <-- 5. 권한 조회 기록

            String accessToken = jwtTokenProvider.createToken(member.getLoginId(), roles);
            String refreshToken = jwtTokenProvider.createToken(member.getLoginId(), roles);
            MDCHelper.appendDebug(LoginService.class, "Access/Refresh 토큰 생성 완료"); // <-- 6. 토큰 생성 기록

            // Redis에 리프레시 토큰 저장 (만료 시간 설정)
            redisTemplate.opsForValue().set(
                    member.getLoginId(),
                    refreshToken,
                    jwtTokenProvider.getRefreshTokenValidityInMilliseconds(),
                    TimeUnit.MILLISECONDS
            );
            MDCHelper.appendDebug(LoginService.class, "Refresh 토큰 Redis 저장 완료"); // <-- 7. Redis 저장 기록

            MDCHelper.appendDebug(LoginService.class, "로그인 처리 성공, 응답 반환"); // <-- 8. 최종 성공 기록
            return new LoginResponse(accessToken, refreshToken);
        } catch (IllegalArgumentException e) {
            logger.error("Login failed: {}", e.getMessage());
            MDCHelper.appendDebug(LoginService.class, "로그인 처리 중 오류 발생: " + e.getMessage()); // <-- 오류 발생 기록
            throw e; // Re-throw the exception so it can be handled by Spring's exception handling
        } finally {
            MDC.remove("loginId");
        }
    }
}
