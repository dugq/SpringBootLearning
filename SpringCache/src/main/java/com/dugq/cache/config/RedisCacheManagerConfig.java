package com.dugq.cache.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisCacheManagerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RedisCacheProperties redisCacheProperties(){
        return new RedisCacheProperties();
    }

    @Bean
    MyRedisCacheManager myRedisCacheManager(RedisConnectionFactory connectionFactory, ObjectProvider<CacheProperties> cacheProperties){
        RedisCacheWriter writer = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        Duration timeToLive = cacheProperties.getObject().getRedis().getTimeToLive();
        if (timeToLive==null){
            timeToLive = Duration.ofSeconds(RandomUtils.nextInt(5*60,10*60));
        }
        String keyPrefix = applicationName;
        if(keyPrefix==null || keyPrefix.isEmpty()){
            keyPrefix = cacheProperties.getObject().getRedis().getKeyPrefix();
        }
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .computePrefixWith(new RedisCacheKeyPrefixGen(keyPrefix));
        if (!cacheProperties.getObject().getRedis().isCacheNullValues()){
            configuration = configuration.disableCachingNullValues();
        }
        return new MyRedisCacheManager(writer,configuration,redisCacheProperties());
    }

}
