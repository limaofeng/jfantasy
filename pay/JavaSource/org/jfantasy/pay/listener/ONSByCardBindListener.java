package org.jfantasy.pay.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import org.hibernate.Session;
import org.jfantasy.framework.autoconfigure.PayAutoConfiguration;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.lucene.dao.hibernate.OpenSessionUtils;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.event.CardBindEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 发送消息 - 阿里云
 */
@Component
public class ONSByCardBindListener implements ApplicationListener<CardBindEvent> {

    @Value("${aliyun.ons.pay.topicId:T-PAY}")
    private String topicId;

    private final Producer producer;

    @Autowired
    public ONSByCardBindListener(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void onApplicationEvent(CardBindEvent event) {
        Session session = OpenSessionUtils.openSession();
        try {
            Card card = event.getCard();
            ThreadJacksonMixInHolder holder = ThreadJacksonMixInHolder.getMixInHolder();
            holder.addAllowPropertyNames(CardDesign.class, "styles", "extras");
            holder.addIgnorePropertyNames(Card.class, "type","batch", Card.BASE_FIELDS);
            Message msg = new Message(topicId, PayAutoConfiguration.ONS_TAGS_CARDBIND_KEY, card.getNo(), JSON.serialize(card).getBytes());
            producer.send(msg);
        } finally {
            OpenSessionUtils.closeSession(session);
        }
    }

}
