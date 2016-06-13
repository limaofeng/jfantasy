package org.jfantasy.pay.listener;

import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.enums.AccountType;
import org.jfantasy.pay.event.PayStatusEvent;
import org.jfantasy.pay.event.context.PayStatus;
import org.jfantasy.pay.service.AccountService;
import org.jfantasy.pay.service.ProjectService;
import org.jfantasy.pay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PayStatusListener implements ApplicationListener<PayStatusEvent> {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AccountService accountService;

    @Override
    public void onApplicationEvent(PayStatusEvent event) {
        PayStatus payStatus = event.getSource();
        Payment payment = payStatus.getPayment();

        switch (payStatus.getStatus()) {
            case ready:
                Account from = accountService.findUnique(AccountType.personal, "member:15921884771");
                Account to = accountService.findUnique(AccountType.enterprise, "shzbsg");
                Properties properties = new Properties();
                transactionService.thirdparty(projectService.get(payStatus.getOrder().getType()), from.getSn(), to.getSn(), payment.getTotalAmount(), "", properties);
                break;
            default:
                System.err.println("请完善逻辑:" + payStatus.getStatus());
        }
    }

}
