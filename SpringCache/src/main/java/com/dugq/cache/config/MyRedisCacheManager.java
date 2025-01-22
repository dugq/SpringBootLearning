package com.dugq.cache.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Objects;

/**
 * 让ttl 和 cacheNull 可以根据key进行定制，而不是一个manager下的所有key都通用
 */
public class MyRedisCacheManager extends RedisCacheManager{

    private final RedisCacheProperties redisCacheProperties;
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, RedisCacheProperties redisCacheProperties) {
        super(cacheWriter, defaultCacheConfiguration);
        this.redisCacheProperties = redisCacheProperties;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    protected RedisCache getMissingCache(String name) {
        RedisCacheProperties.CacheManager cacheManager = this.redisCacheProperties.getManagers().get(name);
        if (cacheManager==null){
            return super.createRedisCache(name,null);
        }
        Duration ttl = defaultCacheConfiguration.getTtl();
        if (cacheManager.getTtl()>0){
            ttl = Duration.ofSeconds(cacheManager.getTtl());
        }
        RedisCacheConfiguration configuration =
                RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .prefixCacheNameWith(defaultCacheConfiguration.getKeyPrefixFor(name));

        if (Objects.isNull(cacheManager.getCacheNull()) && defaultCacheConfiguration.getAllowCacheNullValues()){
            configuration = configuration.disableCachingNullValues();
        }else if(Objects.nonNull(cacheManager.getCacheNull()) && cacheManager.getCacheNull()){
            configuration = configuration.disableCachingNullValues();
        }
        return super.createRedisCache(name,configuration);
    }
}
