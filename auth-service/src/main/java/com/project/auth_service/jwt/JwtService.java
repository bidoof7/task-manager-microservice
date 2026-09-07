package com.project.auth_service.jwt;

import com.project.auth_service.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
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

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        String role = null;
        List<String> permissions = new ArrayList<>();

        for (GrantedAuthority authority : customUserDetails.getAuthorities()) {
            String value = authority.getAuthority();

            assert value != null;
            if (value.startsWith("ROLE_")) {
                role = value.substring(5);
            } else {
                permissions.add(value);
            }
        }

        return Jwts.builder()
                .subject(userDetails.getUsername())
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

    public boolean isTokenValid(UserDetails userDetails, String token) {
        String username = extractAllClaims(token).getSubject();

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
