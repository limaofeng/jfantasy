package org.jfantasy.member.rest.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jfantasy.member.bean.Wallet;
import org.springframework.hateoas.ResourceSupport;

public class WalletResource extends ResourceSupport {

    private Wallet wallet;

    @JsonCreator
    public WalletResource(Wallet wallet) {
        this.wallet = wallet;
    }

    @JsonUnwrapped
    public Wallet getWallet() {
        return this.wallet;
    }

}
