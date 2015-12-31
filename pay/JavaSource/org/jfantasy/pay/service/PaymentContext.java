package org.jfantasy.pay.service;

import com.fantasy.common.order.OrderUrls;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.product.PayResult;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;

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
    private PayConfig payConfig;
    /**
     * 支付产品
     */
    private PayProduct payProduct;
    /**
     * 支付订单 Service
     */
    private OrderService orderService;
    /**
     * 支付结果
     */
    private PayResult payResult;

    private PayProductConfiguration configuration;

    private PayProductConfiguration configuration() {
        if (configuration == null) {
            return configuration = SpringContextUtil.getBeanByType(PayProductConfiguration.class);
        }
        return configuration;
    }

    private PaymentContext(Payment payment, OrderService orderService) throws PayException {
        this.payment = payment;
        this.payConfig = payment.getPayConfig();
        this.payProduct = configuration().loadPayProduct((this.payConfig.getPayProductId()));
        if (this.payProduct == null) {
            throw new PayException("支付产品不存在!");
        }
        this.orderService = orderService;
        this.orderDetails = orderService.loadOrder(payment.getOrderSn());

        this.initOrderUrls();
    }

    public static PaymentContext newInstall(Payment payment, OrderService orderService) throws PayException {
        PaymentContext context = getContext();
        if (context == null) {
            setContext(new PaymentContext(payment, orderService));
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
        this.payConfig = payment.getPayConfig();
    }

    public Order getOrderDetails() {
        return orderDetails;
    }


    public void initOrderUrls() {
        OrderUrls orderUrls = orderService.getOrderUrls();
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

    public PayConfig getPaymentConfig() {
        return payConfig;
    }

    public PayProduct getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(PayProduct payProduct) {
        this.payProduct = payProduct;
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
