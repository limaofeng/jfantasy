package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.framework.autoconfigure.PayAutoConfiguration;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.event.listener.PayRefundListener;
import org.jfantasy.pay.order.TestOrderService;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通过阿里云的ONS传递成功消息
 */
@Component
public class ONSProducerPayRefundListener extends PayRefundListener {

    @Value("${aliyun.ons.pay.topicId}")
    private String topicId;

    @Resource(name = "payProducer")
    public Producer producer;

    @Override
    public boolean supportsOrderType(String orderType) {
        return true;
    }

    @Override
    public void on(Refund refund, Order order) {
        RefundDetails details = new RefundDetails();
        BeanUtil.copyProperties(details, refund);
        details.setOrderKey(OrderKey.newInstance(refund.getOrder().getType(), refund.getOrder().getSn()));
        details.setPayConfigId(refund.getPayConfig().getId());

        if(TestOrderService.ORDER_TYPE.equals(order.getType())){
            TestOrderService.getInstance().on(details.getOrderKey(),details,details.getMemo());
        }else {
            Message msg = new Message(topicId, PayAutoConfiguration.ONS_TAGS_PAY, PayAutoConfiguration.ONS_TAGS_PAY_REFUNDKEY, JSON.serialize(details).getBytes());
            producer.send(msg);
        }
    }

}
