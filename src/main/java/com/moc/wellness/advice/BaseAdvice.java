package com.moc.wellness.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseAdvice {


    public ResponseEntity<Map<String, Object>> handleWithMessage(HttpStatus status, Exception error, WebRequest request) {
        Map<String, Object> resp = respMapWithMessage(status, error, request);

        return new ResponseEntity<>(resp, status);
    }

    protected Map<String, Object> respMapWithMessage(HttpStatus status, Exception error, WebRequest request) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", error.getMessage());
        resp.put("timestamp", Instant.now().toString());
        resp.put("error", status.getReasonPhrase());
        resp.put("status", status.value());
        resp.put("path", request.getDescription(false).replace("uri=", ""));
        return resp;
    }

}
