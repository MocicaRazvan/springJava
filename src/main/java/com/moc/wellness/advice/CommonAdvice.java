package com.moc.wellness.advice;

import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.action.SubEntityNotOwner;
import com.moc.wellness.exception.common.IdNameException;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonAdvice extends BaseAdvice {
    @ApiResponse(responseCode = "400", description = "Validation failed", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/ValidationResponse"))})
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validationError(MethodArgumentNotValidException error, WebRequest request) {

        Map<String, Object> res = new HashMap<>();
        Map<String, Object> reasons = new HashMap<>();

        error.getBindingResult().getFieldErrors().forEach(fieldError -> {
            reasons.put(fieldError.getField(), fieldError.getDefaultMessage());
        });


        res.put("reasons", reasons);
        res.putAll(respMapWithMessage(HttpStatus.BAD_REQUEST, error, request));

        return ResponseEntity.badRequest().body(res);
    }

    @ApiResponse(responseCode = "400", description = "Database constraints violated", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/WithMessage"))})
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, IllegalActionException.class,
            PropertyReferenceException.class, NoSuchElementException.class, NoSuchFieldException.class, SQLException.class})
    public ResponseEntity<Map<String, Object>> sqlIntegrity(Exception error, WebRequest request) {

        return handleWithMessage(HttpStatus.BAD_REQUEST, error, request);

    }

    @ApiResponse(responseCode = "400", description = "Entities related problems", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/WithMessage"))})
    @ExceptionHandler(IdNameException.class)
    public ResponseEntity<Map<String, Object>> notFoundEntity(IdNameException error, WebRequest request) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("name", error.getName());
        resp.put("id", error.getId());
        resp.put("message", error.getMessage());
        resp.putAll(respMapWithMessage(HttpStatus.BAD_REQUEST, error, request));
        return ResponseEntity.badRequest().body(resp);
    }

    @ApiResponse(responseCode = "400", description = "User is not authorize", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/WithMessage"))})
    @ExceptionHandler({PrivateRouteException.class, JwtException.class})
    public ResponseEntity<Map<String, Object>> privateRoute(RuntimeException error, WebRequest request) {
        return handleWithMessage(HttpStatus.FORBIDDEN, error, request);
    }

    @ApiResponse(responseCode = "400", description = "User does not have permissions on this entity", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(ref = "#/components/schemas/WithMessage"))})
    @ExceptionHandler(SubEntityNotOwner.class)
    public ResponseEntity<Map<String, Object>> subEntityNotMatch(SubEntityNotOwner error, WebRequest request) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("expectedUserId", error.getEntityUserId());
        resp.put("receivedUserId", error.getAuthId());
        resp.put("entityId", error.getEntityId());
        resp.putAll(respMapWithMessage(HttpStatus.BAD_REQUEST, error, request));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }


}
