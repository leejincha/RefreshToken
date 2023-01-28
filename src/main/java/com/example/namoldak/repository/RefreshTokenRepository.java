package com.example.namoldak.repository;

import com.example.namoldak.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

// 기능 : Redis에 Refresh Token 저장
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private long expiredTime = 3 * 60L; // 리프레시토큰 유효시간 : 3분

    // 특정 RefreshToken 조회
    public RefreshToken findByEmail(String email){
        RefreshToken refreshToken = new RefreshToken(redisTemplate.opsForValue().get(email), email);
        return refreshToken;
    }

    // RefreshToken 저장
    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        redisTemplate.opsForValue().set(refreshToken.getEmail(), refreshToken.getRefreshToken(),expiredTime, TimeUnit.SECONDS); // 리프레시 토큰 유효시간 설정 : 3분 이부분 추후에 수정 필요 !
        return refreshToken;
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String email){
        redisTemplate.delete(email);
    }

}
