package com.fantasy.payment.service;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.payment.product.*;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付配置
 */
public class PaymentConfiguration implements InitializingBean {

    /**
     * 所有支持的支付产品
     */
    private List<PaymentProduct> paymentProducts = new ArrayList<PaymentProduct>();

    public void setPaymentProducts(List<PaymentProduct> paymentProducts) {
        this.paymentProducts = paymentProducts;
    }

    public PaymentProduct getPaymentProduct(String paymentProductId) {
        return ObjectUtil.find(this.paymentProducts, "id", paymentProductId);
    }

    // 获取所有支付产品集合
    public List<PaymentProduct> getPaymentProducts() {
        return this.paymentProducts;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //支付宝即时交易
        if (ObjectUtil.find(this.paymentProducts, "id", "alipayDirect") == null) {
            AlipayDirect alipayDirect = new AlipayDirect();
            alipayDirect.setId("alipayDirect");
            alipayDirect.setName("支付宝（即时交易）");
            alipayDirect.setBargainorIdName("合作身份者ID");
            alipayDirect.setBargainorKeyName("安全校验码");
            alipayDirect.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            alipayDirect.setLogoPath("/template/tocer/images/payment/alipay_direct_icon.gif");
            alipayDirect.setDescription("支付宝即时交易，付款后立即到账，无预付/年费，单笔费率阶梯最低0.7%，无流量限制。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(alipayDirect);
        }

        //支付宝WAP即时交易
        if (ObjectUtil.find(this.paymentProducts, "id", "alipayDirectByWap") == null) {
            AlipayDirectByWap alipayDirectByWap = new AlipayDirectByWap();
            alipayDirectByWap.setId("alipayDirectByWap");
            alipayDirectByWap.setName("支付宝（WAP即时交易）");
            alipayDirectByWap.setBargainorIdName("合作身份者ID");
            alipayDirectByWap.setBargainorKeyName("安全校验码");
            alipayDirectByWap.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            alipayDirectByWap.setLogoPath("/template/tocer/images/payment/alipay_direct_icon.gif");
            alipayDirectByWap.setDescription("支付宝即时交易，付款后立即到账，无预付/年费，单笔费率阶梯最低0.7%，无流量限制。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(alipayDirectByWap);
        }

        //支付宝担保交易
        if (ObjectUtil.find(this.paymentProducts, "id", "alipayPartner") == null) {
            AlipayPartner alipayPartner = new AlipayPartner();
            alipayPartner.setId("alipayPartner");
            alipayPartner.setName("支付宝（担保交易）");
            alipayPartner.setBargainorIdName("合作身份者ID");
            alipayPartner.setBargainorKeyName("安全校验码");
            alipayPartner.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            alipayPartner.setLogoPath("/template/tocer/images/payment/alipay_partner_icon.gif");
            alipayPartner.setDescription("支付宝担保交易，买家先付款到支付宝，支付宝收到买家付款后即时通知卖家发货，买家收到货物满意后通知支付宝付款给卖家。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(alipayPartner);
        }

        //财付通即时交易
        if (ObjectUtil.find(this.paymentProducts, "id", "tenpayDirect") == null) {
            TenpayDirect tenpayDirect = new TenpayDirect();
            tenpayDirect.setId("tenpayDirect");
            tenpayDirect.setName("财付通（即时交易）");
            tenpayDirect.setBargainorIdName("商户号");
            tenpayDirect.setBargainorKeyName("安全校验码");
            tenpayDirect.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            tenpayDirect.setLogoPath("/template/tocer/images/payment/tenpay_direct_icon.gif");
            tenpayDirect.setDescription("中国领先的在线支付平台，致力于为互联网用户和企业提供安全、便捷、专业的在线支付服务。 <a href=\"http://union.tenpay.com/mch/mch_register.shtml?sp_suggestuser=admin@shopxx.net\" class=\"red\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(tenpayDirect);
        }

        //财付通（担保交易）
        if (ObjectUtil.find(this.paymentProducts, "id", "tenpayPartner") == null) {
            TenpayPartner tenpayPartner = new TenpayPartner();
            tenpayPartner.setId("tenpayPartner");
            tenpayPartner.setName("财付通（担保交易）");
            tenpayPartner.setBargainorIdName("商户号");
            tenpayPartner.setBargainorKeyName("安全校验码");
            tenpayPartner.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            tenpayPartner.setLogoPath("/template/tocer/images/payment/tenpay_partner_icon.gif");
            tenpayPartner.setDescription("中国领先的在线支付平台，致力于为互联网用户和企业提供安全、便捷、专业的在线支付服务。 <a href=\"http://union.tenpay.com/mch/mch_register.shtml?sp_suggestuser=admin@shopxx.net\" class=\"red\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(tenpayPartner);
        }

        //易宝支付
        if (ObjectUtil.find(this.paymentProducts, "id", "yeepay") == null) {
            Yeepay yeepay = new Yeepay();
            yeepay.setId("yeepay");
            yeepay.setName("易宝支付");
            yeepay.setBargainorIdName("商户编号");
            yeepay.setBargainorKeyName("密钥");
            yeepay.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            yeepay.setLogoPath("/template/tocer/images/payment/yeepay_icon.gif");
            yeepay.setDescription("中国领先的独立第三方支付平台，致力于为广大商家和消费者提供“安全、简单、快乐”的专业电子支付解决方案和服务。");
            this.paymentProducts.add(yeepay);
        }

        //快钱
        if (ObjectUtil.find(this.paymentProducts, "id", "pay99bill") == null) {
            Pay99bill pay99bill = new Pay99bill();
            pay99bill.setId("pay99bill");
            pay99bill.setName("快钱");
            pay99bill.setBargainorIdName("账户号");
            pay99bill.setBargainorKeyName("密钥");
            pay99bill.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            pay99bill.setLogoPath("/template/tocer/images/payment/pay99bill_icon.gif");
            pay99bill.setDescription("快钱是国内领先的独立第三方支付企业，旨在为各类企业及个人 提供安全、便捷和保密的综合电子支付服务。");
            this.paymentProducts.add(pay99bill);
        }
    }

    public static List<PaymentProduct> paymentProducts() {
        return SpringContextUtil.getBeanByType(PaymentConfiguration.class).getPaymentProducts();
    }

    public static PaymentProduct paymentProduct(String paymentProductId) {
        return SpringContextUtil.getBeanByType(PaymentConfiguration.class).getPaymentProduct(paymentProductId);
    }

}
