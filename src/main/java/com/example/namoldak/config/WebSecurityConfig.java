package com.example.namoldak.config;

import com.example.namoldak.util.GlobalResponse.GlobalResponseDto;
import com.example.namoldak.util.GlobalResponse.code.StatusCode;
import com.example.namoldak.util.jwt.JwtAuthFilter;
import com.example.namoldak.util.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.example.namoldak.util.GlobalResponse.code.StatusCode.INVALID_TOKEN;

// 기능 : Spring Security 사용에 필요한 설정
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers("/ws-stomp").permitAll()
//                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);
        http.cors();

        // Spring Security에서 발생하는 예외를 커스텀 핸들링
        http.exceptionHandling()
                // 인증부분  (여기서만 사용하기 때문에 익명함수 람다식 이용)
                .authenticationEntryPoint((request, response, authException) -> {
                    securityExceptionResponse(response, INVALID_TOKEN);
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://charleybucket.s3-website.ap-northeast-2.amazonaws.com"); //요거 변경하시면 됩니다.
//        config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
        config.addExposedHeader("Access_Token");
        config.addExposedHeader("Refresh_Token");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.validateAllowCredentials();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
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