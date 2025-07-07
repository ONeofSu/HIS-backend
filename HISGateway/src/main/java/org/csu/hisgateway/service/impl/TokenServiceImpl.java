package org.csu.hisgateway.service.impl;

import org.csu.hisgateway.service.TokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public TokenServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean isInBlackList(String token) {
        String key = "exit:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
