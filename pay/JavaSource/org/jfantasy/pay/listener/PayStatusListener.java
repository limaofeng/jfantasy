package org.jfantasy.pay.listener;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.PayMethod;
import org.jfantasy.pay.bean.enums.TxStatus;
import org.jfantasy.pay.event.PayStatusEvent;
import org.jfantasy.pay.event.context.PayStatus;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PayStatusListener implements ApplicationListener<PayStatusEvent> {

    @Override
    public void onApplicationEvent(PayStatusEvent event) {
        PayStatus payStatus = event.getSource();
        Payment payment = payStatus.getPayment();
        if (payment.getPayConfig().getPayMethod() == PayMethod.wallet) {
            return;
        }
        Transaction transaction = payment.getTransaction();
        switch (payStatus.getStatus()) {
            case ready:
                break;
            case success:
                //更新交易状态
                transaction.setStatus(TxStatus.success);
                break;
            case close:
                transaction.setStatus(TxStatus.close);
                break;
            default:
                System.err.println("请完善逻辑:" + payStatus.getStatus());
        }
    }

}
