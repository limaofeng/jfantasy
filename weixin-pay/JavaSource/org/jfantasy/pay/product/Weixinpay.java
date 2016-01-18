package org.jfantasy.pay.product;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;

import java.util.Map;

public class Weixinpay extends PayProductSupport {com.fantasy.system.job.DataDictJob

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        return false;
    }

    @Override
    public String web(Order order, Payment payment)throws PayException {
        return null;
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public String app(Order order, Payment payment) throws PayException {
        return null;
    }

    @Override
    public String asyncNotify() {
        return null;
    }

    @Override
    public String syncNotify() {
        return null;
    }

}
