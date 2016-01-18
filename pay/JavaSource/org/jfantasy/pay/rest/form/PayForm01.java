package org.jfantasy.pay.rest.form;

import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.product.PayType;

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
    @ApiModelProperty(value = "付款人",required = false)
    private String payer;

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

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
