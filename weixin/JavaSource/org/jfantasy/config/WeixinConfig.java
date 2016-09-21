package org.jfantasy.config;

import org.jfantasy.wx.framework.event.WeiXinEventListener;
import org.jfantasy.wx.framework.factory.WeiXinSessionFactoryBean;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.listener.SubscribeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {"org.jfantasy.wx"})
public class WeixinConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public WeixinConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SubscribeListener subscribeListener() {
        return new SubscribeListener();
    }

    @Bean
    public WeiXinSessionFactoryBean weiXinSessionFactoryBean() {
        WeiXinSessionFactoryBean weiXinSessionFactoryBean = new WeiXinSessionFactoryBean();
        weiXinSessionFactoryBean.setApplicationContext(applicationContext);

        Map<EventMessage.EventType, List<WeiXinEventListener>> events = new HashMap<>();

        List<WeiXinEventListener> weiXinEventListeners = new ArrayList<>();
        weiXinEventListeners.add(subscribeListener());

        events.put(EventMessage.EventType.subscribe, weiXinEventListeners);
        //关注时,记录粉丝信息
        weiXinSessionFactoryBean.setEventListeners(events);

        return weiXinSessionFactoryBean;
    }

}
