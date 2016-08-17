package org.jfantasy.framework.autoconfigure;


import org.jfantasy.framework.security.config.SpringSecurityConfig;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.oauth.AccessTokenCache;
import org.jfantasy.security.data.DefaultSecurityStorage;
import org.jfantasy.security.data.SecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Import(SpringSecurityConfig.class)
@EnableConfigurationProperties({RedisOAuthSettings.class, HttpOAuthSettings.class, ApiGatewaySettings.class})
public class OAuthApiAutoConfiguration {

    @Autowired
    private RedisOAuthSettings redisSettings;
    @Autowired
    private HttpOAuthSettings httpSettings;

    @Bean(name = "oauthRedisConnectionFactory")
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(redisSettings.getHost());
        if (StringUtil.isNotBlank(redisSettings.getPassword())) {
            connectionFactory.setPassword(redisSettings.getPassword());
        }
        connectionFactory.setPort(redisSettings.getPort());
        return connectionFactory;
    }

    @Bean(name = "oauthRedisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }

    @Bean
    public SecurityStorage securityStorage() {
        return new DefaultSecurityStorage(redisTemplate());
    }

    @Bean
    public AccessTokenCache accessTokenCache() {
        return new AccessTokenCache(httpSettings);
    }

}
