package com.moc.wellness.service.impl;

import com.moc.wellness.dto.auth.AuthResponse;
import com.moc.wellness.dto.auth.LoginRequest;
import com.moc.wellness.dto.auth.RegisterRequest;
import com.moc.wellness.mapper.UserMapper;
import com.moc.wellness.model.user.JwtToken;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.JwtTokenRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.AuthService;
import com.moc.wellness.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        UserCustom user = userMapper.fromRegisterRequestToUserCustom(registerRequest);
        UserCustom savedUser = userRepository.save(user);
        return generateResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        UserCustom savedUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        revokeOldTokens(savedUser);

        if (!passwordEncoder.matches(loginRequest.getPassword(), savedUser.getPassword())) {
            throw new UsernameNotFoundException("User not found");
        }

        return generateResponse(savedUser);
    }

    private AuthResponse generateResponse(UserCustom user) {
        final String jwt = jwtUtils.generateToken(user);
        JwtToken jwtToken = JwtToken.builder()
                .user(user)
                .token(jwt)
                .revoked(false)
                .build();
        jwtTokenRepository.save(jwtToken);
        AuthResponse authResponse = userMapper.fromUserCustomToAuthResponse(user);
        authResponse.setToken(jwt);
        return authResponse;
    }

    private void revokeOldTokens(UserCustom user) {
        List<JwtToken> tokens = jwtTokenRepository.findAllValidTokensByUser(user.getId());
        if (tokens.isEmpty()) {
            return;
        }
        tokens.forEach(t -> {
            t.setRevoked(true);
        });
        jwtTokenRepository.saveAll(tokens);
    }
}
