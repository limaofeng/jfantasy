package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.framework.autoconfigure.PayAutoConfiguration;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 发送消息 - 阿里云
 */
@Component
public class ONSByTransactionChangedListener implements ApplicationListener<TransactionChangedEvent> {

    @Value("${aliyun.ons.pay.topicId:T-PAY}")
    private String topicId;

    @Autowired
    public Producer producer;

    @Override
    public void onApplicationEvent(TransactionChangedEvent event) {
        Transaction transaction = event.getTransaction();
        Message msg = new Message(topicId, PayAutoConfiguration.ONS_TAGS_ACCOUNT_KEY, transaction.getSn(), JSON.serialize(transaction).getBytes());
        producer.send(msg);
    }

}
