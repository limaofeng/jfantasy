package org.jfantasy.pay.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class PayMessageListener implements MessageListener {

    @Autowired(required = false)
    private WalletService walletService;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        switch (message.getKey()){
            case "transaction":
                walletService.saveOrUpdateBill(JSON.deserialize(Arrays.toString(message.getBody())));
                break;
            case "account":
                walletService.saveOrUpdateWallet(JSON.deserialize(Arrays.toString(message.getBody())));
                break;
            case "growth":
                break;
        }
        return Action.CommitMessage;
    }

}