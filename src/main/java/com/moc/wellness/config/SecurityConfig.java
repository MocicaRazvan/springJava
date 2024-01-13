package com.moc.wellness.config;

import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final LogoutHandler logoutHandler;
    private final AccessDeniedHandler accessDeniedHandler;

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


    private final String[] TRAINER_LIST = {
            "/test/trainer",

            "/posts/create",
            "/posts/update/**",
            "/posts/trainer/**",
            "/posts/delete/**",

            "/exercises/create",
            "/exercises/update/**",
            "/exercises/trainer/**",
            "/exercises/delete/**",

            "/trainings/create",
            "/trainings/update/**",
            "/trainings/trainer/**",
            "/trainings/delete/**",

            "/orders/create",
            "/orders/trainer/**",

    };
    private final String[] ADMIN_LIST = {
            "/test/admin",
            "/posts/admin/**",
            "/exercises/admin/**",
            "/users/admin/**",
            "/trainings/admin/**",
            "/orders/admin/**",
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(amr -> {
                    amr.requestMatchers(AUTH_WHITELIST).permitAll()
                            .requestMatchers(TRAINER_LIST).hasAnyAuthority(Role.TRAINER.name(), Role.ADMIN.name())
                            .requestMatchers(ADMIN_LIST).hasAuthority(Role.ADMIN.name())
                            .anyRequest().authenticated();

                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(hlc -> {
                    hlc.logoutUrl("/auth/logout")
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler((request, response, authentication) -> {
                                SecurityContextHolder.clearContext();
                            });
                })
                .exceptionHandling(eh -> {
                    eh.accessDeniedHandler(accessDeniedHandler);

                })

                .build();


    }

}
