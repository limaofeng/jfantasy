package com.fantasy.payment.service;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.payment.product.PaymentProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付配置
 */
public class PaymentConfiguration{

    /**
     * 所有支持的支付产品
     */
    private List<PaymentProduct> paymentProducts = new ArrayList<PaymentProduct>();
    private Map<String,PaymentOrderService> paymentOrderServices = new HashMap<String, PaymentOrderService>();

    public void setPaymentProducts(List<PaymentProduct> paymentProducts) {
        this.paymentProducts = paymentProducts;
    }

    public PaymentProduct getPaymentProduct(String paymentProductId) {
        return ObjectUtil.find(this.paymentProducts, "id", paymentProductId);
    }

    public void setPaymentOrderServices(Map<String, PaymentOrderService> paymentOrderServices) {
        this.paymentOrderServices = paymentOrderServices;
    }

    // 获取所有支付产品集合
    public List<PaymentProduct> getPaymentProducts() {
        return this.paymentProducts;
    }

    public PaymentOrderService getPaymentOrderService(String orderType) {
        if(!this.paymentOrderServices.containsKey(orderType)){
            //TODO 添加自定义异常
            throw new RuntimeException("orderType["+orderType+"] 对应的 PaymentOrderService 未配置！");
        }
        return this.paymentOrderServices.get(orderType);
    }

    public static List<PaymentProduct> paymentProducts(){
        return SpringContextUtil.getBeanByType(PaymentConfiguration.class).getPaymentProducts();
    }

    public static PaymentProduct paymentProduct(String paymentProductId){
        return SpringContextUtil.getBeanByType(PaymentConfiguration.class).getPaymentProduct(paymentProductId);
    }

}
