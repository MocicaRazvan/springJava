package com.moc.wellness.controller;

import com.moc.wellness.dto.auth.AuthResponse;
import com.moc.wellness.dto.auth.LoginRequest;
import com.moc.wellness.dto.auth.RegisterRequest;
import com.moc.wellness.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user",
            description = """
                    Unprotected.
                    For the register to be successfully the email should not be already taken
                    It will also be returned a JWT
                    """, responses = {@ApiResponse(description = "The user will be registered to the database and will be sent a JWT",
            responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AuthResponse.class)
    )
    })})
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
//        AuthResponse resp = authService.register(registerRequest);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, createCookie(resp.getToken()).toString())
//                .body(authService.register(registerRequest));
        return authService.register(registerRequest)
                .map(authResponse -> ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, createCookie(authResponse.getToken()).toString())
                        .body(authResponse));

    }

    @Operation(summary = "Login an existing user",
            description = """
                    Unprotected.
                    For the login to be successfully the email and password should be correct
                    It will also be returned a JWT
                    """, responses = {
            @ApiResponse(description = "The user will be registered to the database and will be sent a JWT",
                    responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthResponse.class)
            )
            })
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
//        return ResponseEntity.ok(authService.login(loginRequest));
        return authService.login(loginRequest)
                .map(authResponse -> ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, createCookie(authResponse.getToken()).toString())
                        .body(authResponse));

    }

    private ResponseCookie createCookie(String token) {
        return ResponseCookie.from("authToken", token)
                .httpOnly(true)
                .maxAge(Duration.ofDays(1))
//                .secure(secure)
                .path("/")
                .build();
    }

}
