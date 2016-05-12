package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.event.listener.PayListener;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.rpc.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过阿里云的ONS传递成功消息
 */
@Component
public class ONSProducerPayListener extends PayListener {

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
        details.setOrderKey(OrderKey.newInstance(payment.getOrder().getType(),payment.getOrder().getSn()));
        details.setPayConfigId(payment.getPayConfig().getId());
        Message msg = new Message("TopicTestONS1985", "pay", SerializationUtil.serializer(details));
        msg.setKey("payment");
        producer.send(msg);
    }

}
