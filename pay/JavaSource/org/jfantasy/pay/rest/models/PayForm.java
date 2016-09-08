package org.jfantasy.pay.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.product.Parameters;
import org.jfantasy.pay.product.PayType;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

@ApiModel("支付表单")
public class PayForm implements Serializable {

    private static final long serialVersionUID = -4051043375059966567L;

    @ApiModelProperty(value = "支付配置ID", required = true)
    private Long payconfigId;
    @ApiModelProperty(value = "支付类型", required = true)
    private PayType payType;
    @ApiModelProperty(value = "付款人")
    private String payer;
    @ApiModelProperty(value = "支付参数", notes = "支持的参数:{backUrl:'支付成功后的跳转地址'}")
    private Parameters parameters;

    public Long getPayconfigId() {
        return payconfigId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setPayconfigId(Long payconfigId) {
        this.payconfigId = payconfigId;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    @JsonIgnore
    public Properties getProperties() {
        Properties props = new Properties();
        if (this.parameters == null) {
            return props;
        }
        for (Map.Entry<String, String> entity : this.parameters.entrySet()) {
            props.put(entity.getKey(), entity.getValue());
        }
        return props;
    }
}