package org.jfantasy.pay.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.jfantasy.rpc.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class PayMessageListener implements MessageListener {

    @Autowired
    private OrderService orderService;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        if ("payment".equals(message.getKey())) {
            PaymentDetails details = SerializationUtil.deserializer(message.getBody(), PaymentDetails.class);
            orderService.on(details.getOrderKey(), details, details.getMemo());
        } else if ("refund".equals(message.getKey())) {
            RefundDetails details = SerializationUtil.deserializer(message.getBody(), RefundDetails.class);
            orderService.on(details.getOrderKey(), details, details.getMemo());
        }

        return Action.CommitMessage;
    }

}
