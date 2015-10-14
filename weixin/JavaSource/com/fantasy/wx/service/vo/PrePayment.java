package com.fantasy.wx.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * 微信预支付订单
 */
public class PrePayment {
    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private String orderType;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderSn;
    /**
     * 支付记录流水
     */
    @ApiModelProperty("支付流水")
    private String sn;
    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型", notes = "取值如下：JSAPI，NATIVE，APP，WAP,详细说明见微信官方文档")
    private String tradeType = "JSAPI";
    /**
     * 微信粉丝ID
     */
    @ApiModelProperty("微信粉丝ID")
    private String openid;
    /**
     * 时间戳，自1970年以来的秒数
     */
    @ApiModelProperty("时间戳，自1970年以来的秒数")
    private long timeStamp;
    /**
     * 随机串
     */
    @ApiModelProperty("随机串")
    private String nonceStr;
    /**
     * 订单详情扩展字符串
     */
    @ApiModelProperty("订单详情扩展字符串")
    @JsonProperty("package")
    private String _package;
    /**
     * 微信签名方式
     */
    @ApiModelProperty("微信签名方式")
    private String signType;
    /**
     * 微信签名
     */
    @ApiModelProperty("微信签名")
    private String paySign;

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
