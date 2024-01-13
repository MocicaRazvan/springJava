package com.moc.wellness.utils.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.exception.notFound.TokenNotFound;
import com.moc.wellness.model.user.JwtToken;
import com.moc.wellness.repository.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${security.hs256.key}")
    private String hs256Key;
    private final JwtTokenRepository jwtTokenRepository;


    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }


    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(jwt));
    }


    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }


    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        JwtToken savedToken = jwtTokenRepository.findByToken(jwt).orElseThrow(TokenNotFound::new);
        System.out.println(savedToken);
        if (isTokenExpired(jwt) || savedToken.isRevoked() || !userDetails.getUsername().equals(extractUsername(jwt))) {
            savedToken.setRevoked(true);
            jwtTokenRepository.save(savedToken);
            System.out.println(savedToken);
            throw new TokenNotFound();
        }
        return true;
    }

    public boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(hs256Key));
    }

    public void createResponse(HttpServletResponse response,
                               HttpServletRequest request,
                               HttpStatus status,
                               RuntimeException exception) throws IOException {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("message", exception.getMessage());
        resp.put("timestamp", Instant.now().toString());
        resp.put("error", status.getReasonPhrase());
        resp.put("status", status.value());
        resp.put("path", request.getRequestURI());
        response.setStatus(status.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(resp));


    }

}
