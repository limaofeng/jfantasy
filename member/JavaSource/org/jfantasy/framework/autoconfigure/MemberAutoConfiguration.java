package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.member.ons.OrderMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@ComponentScan("org.jfantasy.member")
@Configuration
@EntityScan("org.jfantasy.member.bean")
public class MemberAutoConfiguration {

    @Autowired
    private AliyunConfiguration aliyunConfiguration;

    @Resource(name = "aliyunSettings")
    private AliyunSettings aliyunSettings;

    @Bean(name = "invoice.aliyunSettings")
    @ConfigurationProperties(prefix = "aliyun.ons.invoice")
    public AliyunSettings ialiyunSettings() {
        return new AliyunSettings(aliyunSettings);
    }

    @Bean(name = "invoice.consumer", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean consumer() {
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription key = new Subscription();
        key.setTopic(ialiyunSettings().getTopicId());
        key.setExpression("*");
        subscriptionTable.put(key, orderMessageListener());
        return aliyunConfiguration.consumer(ialiyunSettings(),subscriptionTable);
    }

    @Bean
    public OrderMessageListener orderMessageListener() {
        return new OrderMessageListener();
    }

}
