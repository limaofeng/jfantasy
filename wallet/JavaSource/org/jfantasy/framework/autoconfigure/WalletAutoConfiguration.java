package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.jfantasy.pay.ons.PayMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ComponentScan("org.jfantasy.pay.ons")
@Configuration
@EntityScan("org.jfantasy.member.bean")
public class WalletAutoConfiguration {

    @Value("${aliyun.ons.pay.accessKey}")
    private String accessKey;
    @Value("${aliyun.ons.pay.secretKey}")
    private String secretKey;

    @Bean(name = "payConsumer",initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean consumer(@Value("${aliyun.ons.pay.consumerId:CID-PAY_WALLET}") String consumerId, @Value("${aliyun.ons.pay.topicId:T-PAY}") String topicId) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, consumerId);
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        consumerBean.setProperties(properties);
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription key = new Subscription();
        key.setTopic(topicId);
        key.setExpression("*");
        subscriptionTable.put(key, payMessageListener());
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

    @Bean
    public PayMessageListener payMessageListener() {
        return new PayMessageListener();
    }

}
