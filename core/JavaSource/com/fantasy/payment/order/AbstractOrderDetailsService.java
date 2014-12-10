package com.fantasy.payment.order;

import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;

/**
 * 实现一个简单的  OrderDetailsService 封装实现
 */
public abstract class AbstractOrderDetailsService implements OrderDetailsService {

    protected final Log LOG = LogFactory.getLog(this.getClass());

    /**
     * 异步通知模板
     */
    private String notifyUrlTemplate;
    /**
     * 支付信息详情页
     */
    private String showPaymentUrlTemplate;
    /**
     * 同步通知模板
     */
    private String returnUrlTemplate;
    /**
     * 订单查看页面模板
     */
    private String showUrlTemplate;

    public void setNotifyUrlTemplate(String notifyUrlTemplate) {
        this.notifyUrlTemplate = notifyUrlTemplate;
    }

    public void setReturnUrlTemplate(String returnUrlTemplate) {
        this.returnUrlTemplate = returnUrlTemplate;
    }

    public void setShowUrlTemplate(String showUrlTemplate) {
        this.showUrlTemplate = showUrlTemplate;
    }

    public void setShowPaymentUrlTemplate(String showPaymentUrlTemplate) {
        this.showPaymentUrlTemplate = showPaymentUrlTemplate;
    }

    @Override
    public String getShowUrl(final String orderSn) {
        return RegexpUtil.replace(this.showUrlTemplate, "\\{orderSn\\}", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return orderSn;
            }
        });
    }

    @Override
    public String getShowPaymentUrl(final String paymentSn) {
        return RegexpUtil.replace(this.showPaymentUrlTemplate, "\\{paymentSn\\}", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return paymentSn;
            }
        });
    }

    @Override
    public String getReturnUrl(final String paymentSn) {
        return RegexpUtil.replace(this.returnUrlTemplate, "\\{paymentSn\\}", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return paymentSn;
            }
        });
    }

    @Override
    public String getNotifyUrl(final String paymentSn) {
        return RegexpUtil.replace(this.notifyUrlTemplate, "\\{paymentSn\\}", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return paymentSn;
            }
        });
    }
}
