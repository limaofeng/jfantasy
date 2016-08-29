package org.jfantasy.member.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.service.InvoiceOrderService;

import javax.annotation.Resource;

public class OrderMessageListener implements MessageListener {

    @Resource
    private InvoiceOrderService invoiceOrderService;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        invoiceOrderService.save(JSON.deserialize(new String(message.getBody()), InvoiceOrder.class));
        return Action.CommitMessage;
    }

}
