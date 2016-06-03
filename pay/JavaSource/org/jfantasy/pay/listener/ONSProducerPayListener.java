package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.event.listener.PayListener;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通过阿里云的ONS传递成功消息
 */
@Component
public class ONSProducerPayListener extends PayListener {

    @Value("${aliyun.ons.pay.topicId}")
    private String topicId;

    @Autowired
    public Producer producer;

    @Override
    public boolean supportsOrderType(String orderType) {
        return true;
    }

    @Override
    public void on(Payment payment, Order order) {
        PaymentDetails details = new PaymentDetails();
        BeanUtil.copyProperties(details, payment);
        details.setOrderKey(OrderKey.newInstance(payment.getOrder().getType(), payment.getOrder().getSn()));
        details.setPayConfigId(payment.getPayConfig().getId());
        Message msg = new Message(topicId, "pay", "payment", JSON.serialize(details).getBytes());
        producer.send(msg);
    }

}
