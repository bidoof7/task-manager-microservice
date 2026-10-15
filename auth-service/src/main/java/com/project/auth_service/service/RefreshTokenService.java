package com.project.auth_service.service;

import com.project.auth_service.exception.InvalidRefreshTokenException;
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
        String refreshToken = UUID.randomUUID().toString();

        String key = PREFIX + refreshToken;

        redisTemplate.opsForValue().set(key, String.valueOf(accountId), Expiration.from(refreshTokenExp, TimeUnit.SECONDS));

        return refreshToken;
    }

    public Long getAccountId(String refreshToken) {
        String key = PREFIX + refreshToken;

        String accountId = redisTemplate.opsForValue().get(key);

        if (accountId == null) {
            throw new InvalidRefreshTokenException();
        }

        return Long.valueOf(accountId);
    }

    public void revoke(String refreshToken) {
        redisTemplate.delete(PREFIX + refreshToken);
    }
}
