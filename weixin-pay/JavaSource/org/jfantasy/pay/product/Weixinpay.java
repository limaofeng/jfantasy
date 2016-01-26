package org.jfantasy.pay.product;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;

import java.util.Map;
import java.util.Properties;

public class Weixinpay extends PayProductSupport {

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        return false;
    }

    @Override
    public String web(Payment payment,Order order, Properties properties)throws PayException {
        return null;
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public String app(Payment payment,Order order) throws PayException {
        return null;
    }

    @Override
    public Payment payNotify(Payment payment, String result) throws PayException{
        return null;
    }

    @Override
    public Refund payNotify(Refund refund, String result) throws PayException {
        return null;
    }


}
