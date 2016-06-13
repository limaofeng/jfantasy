package org.jfantasy.pay.boot;

import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.enums.AccountType;
import org.jfantasy.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountConfiguration  implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        Account from = accountService.findUnique(AccountType.personal, "member:15921884771");
        if(from == null){
            Account account = new Account();
            account.setSn("1920 021 981");
            account.setType(AccountType.personal);
            account.setAmount(BigDecimal.ZERO);
            account.setOwner("member:15921884771");
            accountService.create(account);
        }
        Account to = accountService.findUnique(AccountType.enterprise, "shzbsg");
        if(to == null){
            Account account = new Account();
            account.setSn("1920 021 982");
            account.setType(AccountType.enterprise);
            account.setAmount(BigDecimal.ZERO);
            account.setOwner("shzbsg");
            accountService.create(account);
        }
    }

}
