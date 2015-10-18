package com.fantasy.payment.service;

import com.fantasy.common.order.Order;
import com.fantasy.common.order.OrderService;
import com.fantasy.common.order.OrderUrls;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.product.PayResult;
import com.fantasy.payment.product.PaymentProduct;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付上下文对象
 */
public class PaymentContext {

    private final static Log LOG = LogFactory.getLog(PaymentContext.class);

    private static ThreadLocal<PaymentContext> threadLocal = new ThreadLocal<PaymentContext>();

    private static Handlebars handlebars = new Handlebars();

    private static String serverUrl;

    private Template resultUrlTemplate;
    private Template detailsUrlTemplate;
    private Template notifyUrlTemplate = getTemplate("{{serverUrl}}/pays/{{paymentSn}}/notify");
    private Template returnUrlTemplate = getTemplate("{{serverUrl}}/pays/{{paymentSn}}/return");

    /**
     * 支付订单对象
     */
    private Order orderDetails;
    /**
     * 支付对象
     */
    private Payment payment;
    /**
     * 支付配置
     */
    private PaymentConfig paymentConfig;
    /**
     * 支付产品
     */
    private PaymentProduct paymentProduct;
    /**
     * 支付订单 Service
     */
    private OrderService orderDetailsService;
    /**
     * 支付结果
     */
    private PayResult payResult;

    public static PaymentContext newInstall() {
        PaymentContext context = getContext();
        if (context == null) {
            setContext(new PaymentContext());
        }
        return getContext();
    }

    private static void setContext(PaymentContext context) {
        threadLocal.set(context);
    }

    public static PaymentContext getContext() {
        return threadLocal.get();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        this.paymentConfig = payment.getPaymentConfig();
    }

    public Order getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Order orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDetailsService(OrderService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    public void initOrderUrls() {
        OrderUrls orderUrls = orderDetailsService.getOrderUrls();
        this.detailsUrlTemplate = getTemplate(orderUrls.getDetailsUrl());
        this.resultUrlTemplate = getTemplate(orderUrls.getResultUrl());
    }

    private Template getTemplate(String url) {
        try {
            return handlebars.compileInline(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    private String applyTemplate(Template template, Object o) {
        try {
            return template.apply(o);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public PaymentProduct getPaymentProduct() {
        return paymentProduct;
    }

    public void setPaymentProduct(PaymentProduct paymentProduct) {
        this.paymentProduct = paymentProduct;
    }

    /**
     * 获取支付成功后的异步通知接口地址
     *
     * @return url
     */
    public String getNotifyUrl(String paymentSn) {
        return applyTemplate(notifyUrlTemplate, getData(paymentSn));
    }

    /**
     * 获取支付成功后的回调处理URL
     *
     * @return url
     */
    public String getReturnUrl(String paymentSn) {
        return applyTemplate(returnUrlTemplate, getData(paymentSn));
    }

    /**
     * 订单查看 URL
     *
     * @param orderSn 订单编号
     * @return url
     */
    public String getDetailsUrl(final String orderSn) {
        return applyTemplate(detailsUrlTemplate, new HashMap<String, String>() {
            {
                this.putAll(getData());
                this.put("orderSn", orderSn);
            }
        });
    }

    /**
     * 支付详情页
     *
     * @param paymentSn 用于支付成功后的跳转地址
     * @return url
     */
    public String getResultUrl(String paymentSn) {
        return applyTemplate(resultUrlTemplate, getData(paymentSn));
    }

    /**
     * 支付失败
     *
     * @param payment 支付对象
     */
    public void payFailure(Payment payment) {
        this.orderDetailsService.payFailure(payment);
    }

    /**
     * 支付成功
     *
     * @param payment 支付对象
     */
    public void paySuccess(Payment payment) {
        this.orderDetailsService.paySuccess(payment);
    }

    public PayResult getPayResult() {
        return payResult;
    }

    public void setPayResult(PayResult payResult) {
        this.payResult = payResult;
    }

    private static Map<String, String> getData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("serverUrl", getServerUrl());
        return data;
    }

    private static Map<String, String> getData(String sn) {
        Map<String, String> data = getData();
        data.put("paymentSn", sn);
        return data;
    }

    public static String getServerUrl() {
        return ObjectUtil.defaultValue(serverUrl, serverUrl = PropertiesHelper.load("props/application.properties").getProperty("OrderUrls.serverUrl", WebUtil.getServerUrl(ActionContext.getContext().getHttpRequest())));
    }

}
