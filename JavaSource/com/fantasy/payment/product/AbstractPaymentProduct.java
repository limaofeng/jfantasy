package com.fantasy.payment.product;

import com.fantasy.payment.bean.PaymentConfig;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基类 - 支付产品
 */
public abstract class AbstractPaymentProduct implements PaymentProduct {

    protected static final String RESULT_URL = "/payment/result.do";// 支付结果显示URL

    protected String id;//支付产品ID
    protected String name;// 支付产品名称
    protected String bargainorIdName;// 商户ID参数名称
    protected String description;// 支付产品描述
    protected String bargainorKeyName;// 密钥参数名称
    protected CurrencyType[] currencyTypes;// 支持货币类型
    protected String logoPath;// 支付产品LOGO路径

    /**
     * 获取支付请求URL
     *
     * @return 支付请求URL
     */
    @JsonIgnore
    public abstract String getPaymentUrl();

    /**
     * 获取支付编号
     *
     * @param httpServletRequest httpServletRequest
     * @return 支付编号
     */
    public abstract String getPaymentSn(HttpServletRequest httpServletRequest);

    /**
     * 获取支付金额（单位：元）
     *
     * @param httpServletRequest httpServletRequest
     * @return 支付金额
     */
    public abstract BigDecimal getPaymentAmount(HttpServletRequest httpServletRequest);

    /**
     * 判断是否支付成功
     *
     * @param httpServletRequest httpServletRequest
     * @return 是否支付成功
     */
    public abstract boolean isPaySuccess(HttpServletRequest httpServletRequest);

    /**
     * 获取参数
     *
     * @param paymentSn          支付编号
     * @param paymentAmount      支付金额
     * @param httpServletRequest httpServletRequest
     * @return 在线支付参数
     */
    public abstract Map<String, String> getParameterMap(PaymentConfig paymentConfig, String paymentSn, BigDecimal paymentAmount, HttpServletRequest httpServletRequest);

    /**
     * 验证签名
     *
     * @param httpServletRequest httpServletRequest
     * @return 是否验证通过
     */
    public abstract boolean verifySign(PaymentConfig paymentConfig, HttpServletRequest httpServletRequest);

    /**
     * 根据支付编号获取支付返回信息
     *
     * @param paymentSn 支付编号
     * @return 支付返回信息
     */
    public abstract String getPayreturnMessage(String paymentSn);

    /**
     * 获取支付通知信息
     *
     * @return 支付通知信息
     */
    @JsonIgnore
    public abstract String getPaynotifyMessage();

    /**
     * 根据参数集合组合参数字符串（忽略空值参数）
     *
     * @param params 请求参数
     * @return 参数字符串
     */
    protected String getParameterString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        AtomicReference<StringBuffer> stringBuffer = new AtomicReference<StringBuffer>(new StringBuffer());
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(value)) {
                stringBuffer.get().append("&").append(key).append("=").append(value);
            }
        }
        stringBuffer.get().deleteCharAt(0);
        return stringBuffer.get().toString();
    }

    protected static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBargainorIdName() {
        return bargainorIdName;
    }

    public void setBargainorIdName(String bargainorIdName) {
        this.bargainorIdName = bargainorIdName;
    }

    public String getBargainorKeyName() {
        return bargainorKeyName;
    }

    public void setBargainorKeyName(String bargainorKeyName) {
        this.bargainorKeyName = bargainorKeyName;
    }

    public CurrencyType[] getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(CurrencyType[] currencyTypes) {
        this.currencyTypes = currencyTypes;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}