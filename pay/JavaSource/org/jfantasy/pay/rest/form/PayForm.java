package org.jfantasy.pay.rest.form;

import org.jfantasy.pay.product.Parameters;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.product.PayType;

import java.io.Serializable;

@ApiModel("支付表单")
public class PayForm implements Serializable {
    @ApiModelProperty(value = "订单类型", required = true)
    private String orderType;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderSn;
    @ApiModelProperty(value = "支付配置ID", required = true)
    private Long payconfigId;
    @ApiModelProperty(value = "支付类型", required = true)
    private PayType payType;
    @ApiModelProperty(value = "付款人", required = false)
    private String payer;
    @ApiModelProperty(value = "支付参数", required = false)
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