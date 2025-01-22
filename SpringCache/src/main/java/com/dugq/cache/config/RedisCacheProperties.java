package com.dugq.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.cache.redis")
@Setter
@Getter
public class RedisCacheProperties {
    private Map<String,CacheManager> managers = new HashMap<>();

    @Setter
    @Getter
    class CacheManager{
        private long ttl;
        private Boolean cacheNull;
        private String prefix;

    }
}
