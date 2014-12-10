package com.fantasy.payment.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentConfiguration;
import com.fantasy.payment.order.OrderDetailsService;
import com.fantasy.payment.order.PaymentService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
        OrderDetailsService paymentOrderService = paymentConfiguration.getPaymentOrderService(payment.getOrderType());
        String orderUrl = paymentOrderService.getShowUrl(payment.getOrderSn());
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
    public String submit(String orderType, String orderSn, Long paymentConfigId) throws IOException, PaymentException {
        String sHtmlText = paymentService.submit(orderType, orderSn, paymentConfigId, WebUtil.getParameterMap(this.request));
        this.attrs.put("sHtmlText", sHtmlText);
        return SUCCESS;
    }

    // 支付结果
    public String payreturn(String sn) throws IOException, PaymentException {
        response.getWriter().write(paymentService.payreturn(sn, WebUtil.getParameterMap(this.request)));
        return NONE;
    }

    //支付异步通知
    public String paynotify(String sn) throws IOException, PaymentException {
        response.getWriter().write(paymentService.paynotify(sn, WebUtil.getParameterMap(this.request)));
        return NONE;
    }


    public String index(Pager<Payment> pager, List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Payment> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, paymentService.findPager(pager, filters));
        return JSONDATA;
    }

    public String edit(Long id) {
        this.attrs.put("payment", this.paymentService.get(id));
        return SUCCESS;
    }

    public String delete(Long... ids) {
        this.paymentService.delete(ids);
        return JSONDATA;
    }

}
