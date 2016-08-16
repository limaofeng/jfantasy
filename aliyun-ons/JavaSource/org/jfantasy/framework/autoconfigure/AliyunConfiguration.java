package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.jfantasy.aliyun.AliyunSettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Configuration
public class AliyunConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "aliyun.ons")
    public AliyunSettings aliyunSettings() {
        return new AliyunSettings();
    }

    public ProducerBean producer(AliyunSettings aliyunSettings) {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerId, aliyunSettings.getProducerId());
        properties.setProperty(PropertyKeyConst.AccessKey, aliyunSettings.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, aliyunSettings.getSecretKey());
        producerBean.setProperties(properties);
        return producerBean;
    }

    public ConsumerBean consumer(AliyunSettings aliyunSettings,Map<Subscription, MessageListener> subscriptionTable) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, aliyunSettings.getConsumerId());
        properties.setProperty(PropertyKeyConst.AccessKey, aliyunSettings.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, aliyunSettings.getSecretKey());
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}
