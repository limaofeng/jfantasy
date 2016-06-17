package org.jfantasy.pay.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jfantasy.pay.bean.PayConfig;
import org.springframework.hateoas.ResourceSupport;

public class PayConfigResource extends ResourceSupport {

    private PayConfig payConfig;

    @JsonCreator
    public PayConfigResource(PayConfig payConfig) {
        this.payConfig = payConfig;
    }

    @JsonUnwrapped
    public PayConfig getPayConfig() {
        return this.payConfig;
    }

}
