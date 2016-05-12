package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.event.listener.PayRefundListener;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.jfantasy.rpc.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过阿里云的ONS传递成功消息
 */
@Component
public class ONSProducerPayRefundListener extends PayRefundListener {

    @Autowired
    public Producer producer;

    @Override
    public boolean supportsOrderType(String orderType) {
        return true;
    }

    @Override
    public void on(Refund refund, Order order) {
        RefundDetails details = new RefundDetails();
        BeanUtil.copyProperties(details, refund);
        details.setOrderKey(OrderKey.newInstance(refund.getOrder().getType(),refund.getOrder().getSn()));
        details.setPayConfigId(refund.getPayConfig().getId());
        Message msg = new Message("TopicTestONS1985", "pay", SerializationUtil.serializer(details));
        msg.setKey("refund");
        producer.send(msg);
    }

}
