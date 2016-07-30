package org.jfantasy.pay.rest.models;

import org.jfantasy.pay.bean.enums.AccountType;

public class AccountForm {
    private AccountType type;
    private String owner;
    private String password;

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
