package org.jfantasy.pay.rest.form;

import org.jfantasy.pay.product.Parameters;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.product.PayType;

import java.io.Serializable;

@ApiModel("支付表单")
public class PayForm implements Serializable {
    @ApiModelProperty("订单类型")
    private String orderType;
    @ApiModelProperty("订单编号")
    private String orderSn;
    @ApiModelProperty("支付配置ID")
    private Long payconfigId;
    @ApiModelProperty("支付类型")
    private PayType payType;
    @ApiModelProperty("付款人")
    private String payer;
    @ApiModelProperty("支付参数")
    private Parameters parameters;

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

    public String getParameter(String key) {
        return this.parameters == null ? null : this.parameters.get(key);
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public void addParameter(String key, String value) {
        if (this.parameters == null) {
            this.parameters = new Parameters();
        }
        this.parameters.put(key, value);
    }
}