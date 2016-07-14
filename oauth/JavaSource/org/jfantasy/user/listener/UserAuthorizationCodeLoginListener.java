package org.jfantasy.user.listener;

import org.jfantasy.security.bean.User;
import org.jfantasy.security.context.LoginEvent;
import org.jfantasy.security.data.SecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAuthorizationCodeLoginListener implements ApplicationListener<LoginEvent> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(LoginEvent event) {
        ValueOperations valueOper = redisTemplate.opsForValue();

        User user = event.getUser();
        user.setCode(UUID.randomUUID().toString());

        valueOper.set(SecurityStorage.AUTHORIZATION_CODE_PREFIX + user.getCode(), user);
    }

}
