package com.example.namoldak.repository;

import com.example.namoldak.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

// 기능 : Redis에 Refresh Token 저장
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, RefreshToken> opsHashRefreshToken;

    @PostConstruct
    private void init(){
        opsHashRefreshToken = redisTemplate.opsForHash();
    }

    // 특정 RefreshToken 조회
    public RefreshToken findByEmail(String email){
        return opsHashRefreshToken.get(REFRESH_TOKEN , email);
    }

    // RefreshToken 저장
    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        opsHashRefreshToken.put(REFRESH_TOKEN, refreshToken.getEmail(), refreshToken);
        return refreshToken;
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String refreshTokenId){
        opsHashRefreshToken.delete(REFRESH_TOKEN, refreshTokenId);
    }

}
