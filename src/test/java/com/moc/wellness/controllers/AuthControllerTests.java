package com.moc.wellness.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.controller.AuthController;
import com.moc.wellness.dto.auth.LoginRequest;
import com.moc.wellness.dto.auth.RegisterRequest;
import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.service.AuthService;
import com.moc.wellness.setup.AuthSetup;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests extends AuthSetup {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Register success")
    public void registerSuccess() throws Exception {
        when(authService.register(registerRequest)).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(authResponse)
                        ));


        verify(authService).register(registerRequest);
    }

    @Test
    @DisplayName("Register body not valid")
    public void registerBodyNotValid() throws Exception {

        RegisterRequest notValid = RegisterRequest.builder()
                .firstName("")
                .lastName("")
                .email("email")
                .password("12")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(4)),
                        MockMvcResultMatchers.jsonPath("$.error",
                                CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)));


        verify(authService, times(0)).register(any());
    }

    @Test
    @DisplayName("Login success")
    public void loginSuccess() throws Exception {
        when(authService.login(loginRequest)).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(authResponse)
                        ));

        verify(authService).login(loginRequest);
    }

    @Test
    @DisplayName("Login body not valid")
    public void loginBodyNotValid() throws Exception {
        LoginRequest notValid = LoginRequest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.error",
                                CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)));

        verify(authService, times(0)).login(any());

    }

    @Test
    @DisplayName("Login body user not found")
    public void loginUserNotFound() throws Exception {
        when(authService.login(loginRequest)).
                thenThrow(new UsernameNotFoundException("User not found"));


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpectAll(MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("User not found")),
                        MockMvcResultMatchers.jsonPath("$.error",
                                CoreMatchers.is(HttpStatus.NOT_FOUND.getReasonPhrase())),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(404)));

        verify(authService).login(loginRequest);

    }
}
