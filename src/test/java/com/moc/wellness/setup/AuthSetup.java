package com.moc.wellness.setup;

import com.moc.wellness.dto.auth.AuthResponse;
import com.moc.wellness.dto.auth.LoginRequest;
import com.moc.wellness.dto.auth.RegisterRequest;
import com.moc.wellness.enums.Role;
import com.moc.wellness.mapper.UserMapper;
import com.moc.wellness.model.user.JwtToken;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.JwtTokenRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.impl.AuthServiceImpl;
import com.moc.wellness.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AuthSetup {
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected UserMapper userMapper;
    @Mock
    protected JwtTokenRepository jwtTokenRepository;
    @Mock
    protected PasswordEncoder passwordEncoder;
    @Mock
    protected JwtUtils jwtUtils;
    @InjectMocks
    protected AuthServiceImpl authService;

    protected final Long userId = 1L;

    protected RegisterRequest registerRequest;
    protected LoginRequest loginRequest;
    protected UserCustom user;

    protected final String token = "token";
    protected JwtToken jwtToken;
    protected AuthResponse authResponse;


    @BeforeEach
    public void setUp() {
        loginRequest = LoginRequest.builder()
                .email("razvan@gmail.com")
                .password("1234")
                .build();
        registerRequest = RegisterRequest.builder()
                .email("razvan@gmail.com")
                .password("1234")
                .firstName("Razvan")
                .lastName("Mocica")
                .build();
        user = UserCustom.builder()
                .id(userId)
                .email("razvan@gmail.com")
                .password("1234")
                .firstName("Razvan")
                .lastName("Mocica")
                .role(Role.USER)
                .build();

        jwtToken = JwtToken.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .build();
        authResponse = AuthResponse.builder()
                .email("razvan@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .token(token)
                .build();
    }

}
