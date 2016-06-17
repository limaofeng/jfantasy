package org.jfantasy.pay.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.product.Parameters;
import org.jfantasy.pay.product.PayType;

import java.util.Map;
import java.util.Properties;

/**
 * 用于 /payproducts/{id}/pay 支付的表单
 */
public class PayForm01 {

    @ApiModelProperty(value = "订单类型",required = true)
    private String orderType;
    @ApiModelProperty(value = "订单编号",required = true)
    private String orderSn;
    @ApiModelProperty(value = "支付类型",required = true)
    private PayType payType;
    @ApiModelProperty(value = "付款人")
    private String payer;
    @ApiModelProperty(value = "支付参数", notes = "支持的参数:{backUrl:'支付成功后的跳转地址'}", required = false)
    private Parameters parameters;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getPayer() {
        return payer;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
