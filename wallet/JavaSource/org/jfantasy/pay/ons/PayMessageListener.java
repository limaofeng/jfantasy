package org.jfantasy.pay.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.member.bean.Card;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PayMessageListener implements MessageListener {

    @Autowired
    private WalletService walletService;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        switch (message.getTag()){
            case "transaction":
                walletService.saveOrUpdateBill(JSON.deserialize(new String(message.getBody())));
                break;
            case "account":
                walletService.saveOrUpdateWallet(JSON.deserialize(new String(message.getBody())));
                break;
            case "growth":
                break;
            case "card_bind":
                JsonNode cardbind = JSON.deserialize(new String(message.getBody()));
                Card card = new Card();
                assert cardbind != null;
                card.setAmount(BigDecimal.valueOf(cardbind.get("amount").asDouble()));
                card.setCardNo(cardbind.get("no").asText());
                JsonNode _styles = cardbind.get("design").get("styles");
                if(_styles !=null) {
                    card.setCardStyle(null);
                }
                JsonNode _extras = cardbind.get("design").get("extras");
                if(_styles !=null) {
                    card.setExtras(null);
                }
                String owner = cardbind.get("owner").asText();
                walletService.addCard(owner,card);
                break;
        }
        return Action.CommitMessage;
    }

}