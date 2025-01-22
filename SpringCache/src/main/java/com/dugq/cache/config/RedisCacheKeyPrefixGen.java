package com.dugq.cache.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.CacheKeyPrefix;

@RequiredArgsConstructor
public class RedisCacheKeyPrefixGen implements CacheKeyPrefix {
    String SEPARATOR = ":";

    private final String prefix;

    @Override
    public String compute(String cacheName) {
        return prefix+SEPARATOR+ cacheName+ SEPARATOR;
    }
}
