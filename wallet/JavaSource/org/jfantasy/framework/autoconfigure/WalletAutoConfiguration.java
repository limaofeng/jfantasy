package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.pay.ons.PayMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@ComponentScan("org.jfantasy.pay.ons")
@Configuration
@EntityScan("org.jfantasy.member.bean")
public class WalletAutoConfiguration {

    @Autowired
    private AliyunConfiguration aliyunConfiguration;

    @Resource(name = "aliyunSettings")
    private AliyunSettings aliyunSettings;

    @Bean(name = "wallet.aliyunSettings")
    @ConfigurationProperties(prefix = "aliyun.ons.pay")
    public AliyunSettings aliyunSettings() {
        return new AliyunSettings(aliyunSettings);
    }

    @Bean(name = "pay.consumer",initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean consumer() {
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription key = new Subscription();
        key.setTopic(aliyunSettings().getTopicId());
        key.setExpression("*");
        subscriptionTable.put(key, payMessageListener());
        return aliyunConfiguration.consumer(aliyunSettings(),subscriptionTable);
    }

    @Bean
    public PayMessageListener payMessageListener() {
        return new PayMessageListener();
    }

}
