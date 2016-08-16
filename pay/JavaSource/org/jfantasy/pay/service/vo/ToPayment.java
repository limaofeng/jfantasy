package org.jfantasy.pay.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.pay.bean.Payment;

@JsonIgnoreProperties
public class ToPayment extends Payment {

    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
