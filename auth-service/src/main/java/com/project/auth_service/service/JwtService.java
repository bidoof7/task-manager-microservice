package com.project.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final SecretKey secret;
    private final long exp;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-token-expiration}") long exp) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.exp = exp;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }

    public String generateAccessToken(String email, String role, List<String> permissions) {
        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("permissions", permissions)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + exp))
                .signWith(secret)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
}
