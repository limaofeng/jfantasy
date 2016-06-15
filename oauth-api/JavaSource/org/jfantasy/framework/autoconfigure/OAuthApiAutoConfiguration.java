package org.jfantasy.framework.autoconfigure;


import org.jfantasy.framework.security.config.SpringSecurityConfig;
import org.jfantasy.oauth.userdetails.HttpOAuthUserDetailsService;
import org.jfantasy.oauth.userdetails.RedisOAuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@Import(SpringSecurityConfig.class)
@EnableConfigurationProperties({RedisOAuthSettings.class, HttpOAuthSettings.class})
public class OAuthApiAutoConfiguration {

    @Autowired
    private RedisOAuthSettings redisSettings;
    @Autowired
    private HttpOAuthSettings httpSettings;

    @Bean
    public UserDetailsService userDetailsService() {
        if (!redisSettings.invalid()) {
            JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
            connectionFactory.setHostName(redisSettings.getHost());
            connectionFactory.setPassword(redisSettings.getPassword());
            connectionFactory.setPort(redisSettings.getPort());

            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(connectionFactory);
            new RedisOAuthUserDetailsService(redisTemplate);
        }
        return new HttpOAuthUserDetailsService(httpSettings.getUrl());
    }

}
