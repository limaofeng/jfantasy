package org.jfantasy.oauth.boot;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.member.listener.MemberAuthorizationCodeLoginListener;
import org.jfantasy.oauth.service.ApiKeyService;
import org.jfantasy.oauth.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitTestRunner implements CommandLineRunner {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApiKeyService apiKeyService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(SpringContextUtil.getBeanByType(MemberAuthorizationCodeLoginListener.class));
    }

}
