package org.jfantasy.pay.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.jfantasy.pay.bean.*;
import org.jfantasy.pay.bean.enums.PayMethod;
import org.jfantasy.pay.error.PayException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 基类 - 支付产品
 */
@JsonIgnoreProperties
public abstract class PayProductSupport implements PayProduct {

    static final String PROPERTIES_BACKURL = "back_url";
    static final String PROPERTIES_SHOWURL = "show_url";

    protected final static Log LOG = LogFactory.getLog(PayProductSupport.class);

    protected String id;//支付产品ID
    protected String name;// 支付产品名称
    protected PayMethod payMethod;
    protected String bargainorIdName;// 商户ID参数名称
    protected String summary;//产品摘要
    protected String description;// 支付产品描述
    protected String bargainorKeyName;// 密钥参数名称
    protected CurrencyType[] currencyTypes;// 支持货币类型
    protected String logoPath;// 支付产品LOGO路径
    protected Map<String, ExtProperty> extPropertys;//自定义的属性
    protected String examples;//集成示例

    @Override
    public String refund(Refund refund) {
        throw new PayException(this.getName() + " 的退款逻辑未实现");
    }

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        throw new PayException(this.getName() + " 的 app 支付未实现");
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

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        return null;
    }

    public void log(String tyep, String payType, Payment payment, PayConfig config, String result) {
        MDC.put("type", tyep);
        MDC.put("payType", payType);
        MDC.put("paymentSn", payment.getSn());
        MDC.put("payProductId", config.getPayProductId());
        MDC.put("payConfigId", config.getId());
        MDC.put("body", result);
        LOG.info(MDC.getContext());
    }

    public void log(String tyep, String payType, Refund refund, PayConfig config, String result) {
        MDC.put("type", tyep);
        MDC.put("payType", payType);
        MDC.put("paymentSn", refund.getSn());
        MDC.put("payProductId", config.getPayProductId());
        MDC.put("payConfigId", config.getId());
        MDC.put("body", result);
        LOG.info(MDC.getContext());
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    @JsonAnyGetter
    public Map<String, ExtProperty> getExtPropertys() {
        return extPropertys;
    }

    public void setExtPropertys(Map<String, ExtProperty> extPropertys) {
        this.extPropertys = extPropertys;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    @JsonAnySetter
    public void set(String key, ExtProperty extProperty) {
        if (this.extPropertys == null) {
            this.extPropertys = new LinkedHashMap<>();
        }
        this.extPropertys.put(key, extProperty);
    }

}