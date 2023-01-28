package com.example.namoldak.util.jwt;

import com.example.namoldak.util.GlobalResponse.CustomException;
import com.example.namoldak.util.GlobalResponse.GlobalResponseDto;
import com.example.namoldak.util.GlobalResponse.code.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            securityExceptionResponse(response, e.getStatusCode());
        }
    }

    // 예외 응답처리 메소드
    public void securityExceptionResponse(HttpServletResponse response, StatusCode statusCode) throws IOException {
        response.setStatus(statusCode.getHttpStatus().value());
        response.setHeader("content-type", "application/json; charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(new GlobalResponseDto(statusCode));
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
