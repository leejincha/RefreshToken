package com.example.namoldak.util.jwt;

import com.example.namoldak.util.GlobalResponse.GlobalResponseDto;
import com.example.namoldak.util.GlobalResponse.code.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 기능 : JWT 필터
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String token = jwtUtil.resolveToken(request);
//
//        if(token != null) {
//            if(!jwtUtil.validateToken(token)){
//                response.setStatus(StatusCode.INVALID_TOKEN.getHttpStatus().value());
//                response.setContentType("application/json; charset=UTF-8");
//                try {
//                    String json = new ObjectMapper().writeValueAsString(new GlobalResponseDto(StatusCode.INVALID_TOKEN));
//                    response.getWriter().write(json);
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//                return;
//            }
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

        if (accessToken != null) {
            if (!jwtUtil.validateToken(accessToken)) {
                jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.UNAUTHORIZED);
                return;
            }
            setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));

        } else if (refreshToken != null) {
            if (!jwtUtil.refreshTokenValidation(refreshToken)) {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            // 3. 토큰이 유효하다면 토큰에서 정보를 가져와 Authentication 에 세팅
//            Claims info = jwtUtil.getUserInfoFromToken(token);
//            setAuthentication(info.getSubject());
            setAuthentication(jwtUtil.getUserInfoFromToken(refreshToken));
        }
        // 4. 다음 필터로 넘어간다
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(email);
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String message, HttpStatus statusCode) {
        response.setStatus(statusCode.value());
        response.setContentType("application/json; charset=UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResponseDto<>(StatusCode.BAD_REQUEST_TOKEN));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
