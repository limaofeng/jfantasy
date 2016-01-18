package org.jfantasy.config;

import org.jfantasy.wx.framework.event.WeiXinEventListener;
import org.jfantasy.wx.framework.factory.WeiXinSessionFactoryBean;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.listener.SubscribeListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"org.jfantasy.wx"})
public class WeixinConfig  implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public SubscribeListener subscribeListener(){
        return new SubscribeListener();
    }

    @Bean
    public WeiXinSessionFactoryBean weiXinSessionFactoryBean(){
        WeiXinSessionFactoryBean weiXinSessionFactoryBean = new WeiXinSessionFactoryBean();
        weiXinSessionFactoryBean.setApplicationContext(applicationContext);
        //关注时,记录粉丝信息
        weiXinSessionFactoryBean.setEventListeners(new HashMap<EventMessage.EventType, List<WeiXinEventListener>>(){
            {
                this.put(EventMessage.EventType.subscribe,new ArrayList<WeiXinEventListener>(){
                    {
                        this.add(subscribeListener());
                    }
                });
            }
        });
        return weiXinSessionFactoryBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
