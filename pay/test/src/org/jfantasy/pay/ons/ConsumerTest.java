package org.jfantasy.pay.ons;


import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

public class ConsumerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID-20160429");// ConsumerId需要设置您自己的
        properties.put(PropertyKeyConst.AccessKey, "GjYnEEMsLVTomMzF");// AccessKey 需要设置您自己的
        properties.put(PropertyKeyConst.SecretKey, "rYSFhN67iXR0vl0pUSatSQjEqR2e2F");// SecretKey 需要设置您自己的
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("TopicTestONS1985", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + new String(message.getBody()));
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}
