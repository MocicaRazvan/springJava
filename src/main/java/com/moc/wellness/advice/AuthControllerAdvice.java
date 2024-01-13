package com.moc.wellness.advice;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;


@RestControllerAdvice
public class AuthControllerAdvice extends BaseAdvice {

    @ApiResponse(responseCode = "404", description = "The user credentials are not valid", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/AuthMessage"))})
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(UsernameNotFoundException error, WebRequest request) {
        return handleWithMessage(HttpStatus.NOT_FOUND, error, request);

    }


}
