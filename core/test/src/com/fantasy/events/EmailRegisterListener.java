package com.fantasy.events;

import com.fantasy.security.bean.User;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailRegisterListener implements ApplicationListener<RegisterEvent> {

    @Async
    @Override
    public void onApplicationEvent(final RegisterEvent event) {
        System.out.println("注册成功，发送确认邮件给：" + ((User) event.getSource()).getUsername());
    }

}