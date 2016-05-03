package org.jfantasy.pay.ons;


import com.aliyun.openservices.ons.api.*;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class Consumer {

    @Autowired
    private OrderService orderService;

    public void start(){
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID-20160428");// ConsumerId需要设置您自己的
        properties.put(PropertyKeyConst.AccessKey, "GjYnEEMsLVTomMzF");// AccessKey 需要设置您自己的
        properties.put(PropertyKeyConst.SecretKey, "rYSFhN67iXR0vl0pUSatSQjEqR2e2F");// SecretKey 需要设置您自己的
        com.aliyun.openservices.ons.api.Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("TopicTestONS1985", "payment", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                orderService.on("test:t00001", PaymentStatus.close,"成都市");
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }

}
