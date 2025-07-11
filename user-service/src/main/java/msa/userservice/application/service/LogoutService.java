package msa.userservice.application.service;

import msa.userservice.application.port.in.LogoutUseCase;
import msa.userservice.jwt.JwtTokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LogoutService implements LogoutUseCase {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public LogoutService(RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void logout(String accessToken, String username) {
        // 액세스 토큰 남은 만료 시간 계산
        Long expiration = jwtTokenProvider.getClaims(accessToken).getExpiration().getTime();
        Long now = System.currentTimeMillis();
        Long remainingTime = expiration - now;

        // Redis에 액세스 토큰 블랙리스트로 저장
        redisTemplate.opsForValue().set(accessToken, "logout", remainingTime, TimeUnit.MILLISECONDS);

        // Redis에서 리프레시 토큰 삭제
        redisTemplate.delete(username);
    }
}