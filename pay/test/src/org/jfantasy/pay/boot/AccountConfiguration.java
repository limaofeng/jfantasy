package org.jfantasy.pay.boot;

import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.enums.AccountType;
import org.jfantasy.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountConfiguration  implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        Account from = accountService.findUnique(AccountType.personal, "member:15921884771");
        if(from == null){
            accountService.save(AccountType.personal,"member:15921884771");
        }
        Account to = accountService.findUnique(AccountType.enterprise, "shzbsg");
        if(to == null){
            accountService.save(AccountType.enterprise,"shzbsg");
        }
    }

}
