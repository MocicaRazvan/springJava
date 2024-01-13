package com.moc.wellness.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.exception.notFound.TokenNotFound;
import com.moc.wellness.utils.jwt.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.PathMatcher;
import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    private final String[] AUTH_WHITELIST = {
            "/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/**"
    };

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        AntPathMatcher pathMatcher = new AntPathMatcher();

        if (Arrays.stream(AUTH_WHITELIST).anyMatch(route -> pathMatcher.match(route, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String authHeader = request.getHeader("Authorization");


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new TokenNotFound();
            }
            final String jwt = authHeader.substring(7);
            System.out.println(jwt);
            final String email = jwtUtils.extractUsername(jwt);
            System.out.println(email);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    System.out.println(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (RuntimeException error) {

            jwtUtils.createResponse(response, request, HttpStatus.FORBIDDEN, error);
        }
    }


}
