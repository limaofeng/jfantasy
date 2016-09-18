package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.framework.autoconfigure.PayAutoConfiguration;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.event.AccountChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息 - 阿里云
 */
@Component
public class ONSByPointChangedListener implements ApplicationListener<AccountChangedEvent> {

    @Resource(name = "pay.aliyunSettings")
    private AliyunSettings aliyunSettings;

    private final Producer producer;

    @Autowired
    public ONSByPointChangedListener(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void onApplicationEvent(AccountChangedEvent event) {
        Account account = event.getAccount();
        Message msg = new Message(aliyunSettings.getTopicId(), PayAutoConfiguration.ONS_TAGS_POINT_KEY, account.getSn(), JSON.serialize(account).getBytes());
        producer.send(msg);
    }

}
