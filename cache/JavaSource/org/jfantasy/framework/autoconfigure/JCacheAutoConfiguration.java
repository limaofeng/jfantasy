package org.jfantasy.framework.autoconfigure;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class JCacheAutoConfiguration extends CachingConfigurerSupport {

    private static final Logger logger = Logger.getLogger(JCacheAutoConfiguration.class);

    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean(name = "cacheManager")
    public CacheManager redisCacheManager() {
        return new RedisCacheManager(redisTemplate());
    }

}


