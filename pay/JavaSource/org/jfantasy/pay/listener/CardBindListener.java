package org.jfantasy.pay.listener;

import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.event.CardBindEvent;
import org.jfantasy.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 绑定卡的关联的实际逻辑
 */
@Component
public class CardBindListener implements ApplicationListener<CardBindEvent> {

    @Autowired
    private AccountService accountService;

    @Override
    public void onApplicationEvent(CardBindEvent event) {
        //TODO 卡充值逻辑
        Card card = event.getCard();
    }

}
