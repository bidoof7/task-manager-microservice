package com.project.auth_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    private final String PREFIX = "refresh_token:";

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExp;

    public String create(Long accountId) {
        String token = UUID.randomUUID().toString();

        String key = PREFIX + token;

        redisTemplate.opsForValue().set(key, String.valueOf(accountId), Expiration.from(refreshTokenExp, TimeUnit.SECONDS));

        return token;
    }

    public Long getAccountId(String token) {
        String key = PREFIX + token;

        String accountId = redisTemplate.opsForValue().get(key);

        if (accountId == null) {
            throw new RuntimeException("REFRESH TOKEN INVALID OR EXPIRED");
        }

        return Long.valueOf(accountId);
    }

    public void revoke(String token) {
        redisTemplate.delete(PREFIX + token);
    }
}
