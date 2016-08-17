package org.jfantasy.invoice;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class OrderService {

    @Resource(name = "invoice.producer")
    private Producer producer;
    @Autowired
    private AliyunSettings aliyunSettings;

    public void save(Order order) {
        producer.send(new Message(aliyunSettings.getTopicId(), order.getOrderType(), order.getOrderType() + ":" + order.getOrderSn(), JSON.serialize(order).getBytes()));
    }

}
