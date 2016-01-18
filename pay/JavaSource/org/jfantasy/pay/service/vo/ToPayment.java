package org.jfantasy.pay.service.vo;

import org.jfantasy.pay.bean.Payment;

public class ToPayment extends Payment {

    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
