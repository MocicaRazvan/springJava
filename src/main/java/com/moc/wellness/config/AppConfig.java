package com.moc.wellness.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.JwtTokenRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.utils.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AppConfig {


    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtUtils jwtUtils;


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            final String authHeader = request.getHeader("Authorization");
            final String authCookie = request.getCookies() != null ? Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("authToken"))
                    .findFirst().map(Cookie::getValue).
                    orElse(null)
                    : null;
            if ((authHeader == null || !authHeader.startsWith("Bearer "))
                    && authCookie == null
            ) {
                return;
            }

            final String jwt = authHeader != null ? authHeader.substring(7) : authCookie;
            jwtTokenRepository.findByToken(jwt).ifPresent(savedToken -> {
                savedToken.setRevoked(true);
                jwtTokenRepository.save(savedToken);
                try {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");
                    response.getWriter().write("Token removed!");
                    response.setHeader("Set-Cookie", "authToken=; Max-Age=0; Path=/; HttpOnly; SameSite=Strict");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            jwtUtils.createResponse(response, request, HttpStatus.FORBIDDEN, accessDeniedException);
        };
    }


}