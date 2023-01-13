package com.example.namoldak.domain;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

// 기능 : Redis에 Refresh Token 저장
@Getter
@Setter
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;
    private Long id;
    private String refreshToken;
    private String email;
    public RefreshToken(String token, String email) {
        this.refreshToken = token;
        this.email = email;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
