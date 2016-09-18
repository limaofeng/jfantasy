package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.framework.autoconfigure.PayAutoConfiguration;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.event.TransactionChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息 - 阿里云
 */
@Component
public class ONSByTransactionChangedListener implements ApplicationListener<TransactionChangedEvent> {

    @Resource(name = "pay.aliyunSettings")
    private AliyunSettings aliyunSettings;

    private final Producer producer;

    @Autowired
    public ONSByTransactionChangedListener(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void onApplicationEvent(TransactionChangedEvent event) {
        Transaction transaction = event.getTransaction();
        Message msg = new Message(aliyunSettings.getTopicId(), PayAutoConfiguration.ONS_TAGS_TRANSACTION_KEY, transaction.getSn(), JSON.serialize(transaction).getBytes());
        producer.send(msg);
    }

}
