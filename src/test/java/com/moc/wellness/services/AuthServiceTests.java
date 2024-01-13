package com.moc.wellness.services;

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
import com.moc.wellness.setup.AuthSetup;
import com.moc.wellness.utils.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests extends AuthSetup {


    private void generateResponseMocks() {
        when(jwtUtils.generateToken(user)).thenReturn(token);
        when(jwtTokenRepository.save(jwtToken)).thenReturn(null);
        when(userMapper.fromUserCustomToAuthResponse(user)).thenReturn(authResponse);


    }

    private void generateResponseVerify() {
        verify(jwtTokenRepository).save(jwtToken);
        verify(userMapper).fromUserCustomToAuthResponse(user);
    }

    @Test
    @DisplayName("Register user success")
    public void registerUserSuccess() {
        authResponse.setToken(null);
        when(userMapper.fromRegisterRequestToUserCustom(registerRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        generateResponseMocks();

        AuthResponse exp = authService.register(registerRequest);

        Assertions.assertEquals(exp, authResponse);

        verify(userMapper).fromRegisterRequestToUserCustom(registerRequest);
        verify(userRepository).save(user);
        verify(jwtUtils).generateToken(user);
        generateResponseVerify();

    }

    @Test
    @DisplayName("Login User Success empty tokens")
    public void loginUserSuccessEmptyTokens() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.ofNullable(user));
        when(jwtTokenRepository.findAllValidTokensByUser(user.getId()))
                .thenReturn(Collections.emptyList());
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);
        generateResponseMocks();

        AuthResponse exp = authService.login(loginRequest);

        Assertions.assertEquals(exp, authResponse);

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtTokenRepository).findAllValidTokensByUser(user.getId());
        verify(jwtTokenRepository, times(0)).saveAll(any());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        generateResponseVerify();
    }

    @Test
    @DisplayName("Login User Success valid tokens")
    public void loginUserSuccessValidTokens() {
        String mock = "mock";
        JwtToken mockToken = JwtToken.builder()
                .user(user)
                .token(mock)
                .revoked(false)
                .build();
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.ofNullable(user));
        when(jwtTokenRepository.findAllValidTokensByUser(user.getId())).thenReturn(List.of(mockToken));
        when(jwtTokenRepository.saveAll(List.of(mockToken))).then(invocation -> {
            List<JwtToken> list = invocation.getArgument(0);
            JwtToken savedToken = list.get(0);
            mockToken.setRevoked(true);
            Assertions.assertEquals(savedToken, mockToken);
            return null;
        });


        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);
        generateResponseMocks();

        AuthResponse exp = authService.login(loginRequest);

        Assertions.assertEquals(exp, authResponse);

        verify(userRepository).findByEmail(loginRequest.getEmail());

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtTokenRepository).findAllValidTokensByUser(user.getId());
        verify(jwtTokenRepository).saveAll(List.of(mockToken));
        generateResponseVerify();

    }

    @Test
    @DisplayName("Login user not found")
    public void loginUserNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        UsernameNotFoundException ex = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> authService.login(loginRequest));

        Assertions.assertEquals(ex.getMessage(), "User not found");

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtTokenRepository, times(0)).findAllValidTokensByUser(any());
        verify(jwtTokenRepository, times(0)).saveAll(any());
        verify(passwordEncoder, times(0)).matches(any(), any());
        verify(jwtTokenRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToAuthResponse(any());
    }

    @Test
    @DisplayName("Login user passwords not match")
    public void loginUserPasswordsNotMatch() {
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.ofNullable(user));
        when(jwtTokenRepository.findAllValidTokensByUser(user.getId()))
                .thenReturn(Collections.emptyList());
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(false);

        UsernameNotFoundException ex = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> authService.login(loginRequest));

        Assertions.assertEquals(ex.getMessage(), "User not found");

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtTokenRepository).findAllValidTokensByUser(user.getId());
        verify(jwtTokenRepository, times(0)).saveAll(any());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtTokenRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToAuthResponse(any());
    }
}
