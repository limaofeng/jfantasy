package org.jfantasy.pay.ons;


import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConsumerTest {
    public static void main(String[] args) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, "CID-20160429");
        properties.setProperty(PropertyKeyConst.AccessKey, "GjYnEEMsLVTomMzF");
        properties.setProperty(PropertyKeyConst.SecretKey, "rYSFhN67iXR0vl0pUSatSQjEqR2e2F");
        consumerBean.setProperties(properties);
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription key = new Subscription();
        key.setTopic("TopicTestONS1985");
        key.setExpression("pay");
        subscriptionTable.put(key, new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + new String(message.getBody()));
                return Action.CommitMessage;
            }
        });
        consumerBean.setSubscriptionTable(subscriptionTable);
        consumerBean.start();
        System.out.println("Consumer Started");
    }
}
