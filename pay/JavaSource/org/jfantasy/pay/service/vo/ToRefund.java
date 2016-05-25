package org.jfantasy.pay.service.vo;

import org.jfantasy.pay.bean.Refund;

public class ToRefund extends Refund {

    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

}
