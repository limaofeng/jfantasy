package com.fantasy.payment.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.Payment.PaymentStatus;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.product.PaymentProduct;
import com.fantasy.payment.service.PaymentConfiguration;
import com.fantasy.payment.service.PaymentOrderService;
import com.fantasy.payment.service.PaymentService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 支付处理类
 */
public class PaymentAction extends ActionSupport {

    private static final long serialVersionUID = -4817743897444468581L;

    @Resource
    private PaymentService paymentService;
    @Resource
    private PaymentConfiguration paymentConfiguration;

    public String execute(String sn) throws Exception {
        Payment payment = paymentService.get(sn);
        PaymentOrderService paymentOrderService = paymentConfiguration.getPaymentOrderService(payment.getOrderType());
        String orderUrl = paymentOrderService.url(payment.getOrderSn());
        if (StringUtil.isNotBlank(orderUrl)) {
            response.sendRedirect(orderUrl);
            return NONE;
        } else {
            this.attrs.put("payment", payment);
            return SUCCESS;
        }
    }

    /**
     * 支付提交
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param paymentConfigId 支付方式
     * @return {string}
     */
    public String submit(String orderType, String orderSn, Long paymentConfigId) throws PaymentException {
        Payment payment = paymentService.ready(orderType, orderSn, paymentConfigId);
        PaymentProduct paymentProduct = paymentService.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());

        // 支付参数
        Map<String, String> parameterMap = paymentProduct.getParameterMap(payment.getPaymentConfig(), payment.getSn(), payment.getTotalAmount(), WebUtil.getParameterMap(this.request));
        this.attrs.put("paymentUrl", paymentProduct.getPaymentUrl());
        this.attrs.put("parameterMap", parameterMap);
        return SUCCESS;
    }

    // 支付结果
    public String payreturn(String sn) throws IOException {
        Payment payment = paymentService.get(sn);
        if (payment == null) {
            addActionError("支付记录不存在!");
            return ERROR;
        }
        PaymentProduct paymentProduct = paymentConfiguration.getPaymentProduct(payment.getPaymentConfig().getPaymentProductId());
        if (paymentProduct == null) {
            addActionError("支付产品不存在!");
            this.paymentService.failure(sn);
            return ERROR;
        }
//        BigDecimal totalAmount = paymentProduct.getPaymentAmount(this.request);
        boolean isSuccess = paymentProduct.isPaySuccess(WebUtil.getParameterMap(this.request));

        if (!paymentProduct.verifySign(payment.getPaymentConfig(), WebUtil.getParameterMap(this.request))) {
            addActionError("支付签名错误!");
            this.paymentService.failure(sn);
            return ERROR;
        } else if (!isSuccess) {
            addActionError("支付失败!");
            this.paymentService.failure(sn);
            return ERROR;
        } else if (payment.getPaymentStatus() == PaymentStatus.success) {
            this.attrs.put("sn", sn);
            return "result";
        } else {
            paymentService.success(sn);
            response.getWriter().write(paymentProduct.getPayreturnMessage(sn));
            response.flushBuffer();
            return NONE;
        }
    }

    //支付异步通知
    public String paynotify(String sn) throws IOException {
        return this.payreturn(sn);
    }


    public String index(Pager<Payment> pager,List<PropertyFilter> filters){
        this.search(pager,filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Payment> pager,List<PropertyFilter> filters){
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, paymentService.findPager(pager, filters));
        return JSONDATA;
    }

    public String edit(Long id){
        this.attrs.put("payment",this.paymentService.get(id));
        return SUCCESS;
    }

    public String delete(Long... ids){
        this.paymentService.delete(ids);
        return JSONDATA;
    }

}
