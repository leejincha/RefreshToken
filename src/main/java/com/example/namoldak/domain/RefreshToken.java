package com.example.namoldak.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

// 기능 : Redis에 Refresh Token 저장
@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 30L) // 초단위
public class RefreshToken {

    //    private static final long serialVersionUID = 6494678977089006639L;
    @Id
    private String email;
    private String refreshToken;

    public RefreshToken(String email, String token) {
        this.refreshToken = token;
        this.email = email;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}

