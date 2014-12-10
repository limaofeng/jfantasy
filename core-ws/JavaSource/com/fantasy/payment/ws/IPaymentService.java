package com.fantasy.payment.ws;

import com.fantasy.payment.ws.dto.SubmitPaymentDTO;

/**
 * 支付 webservice 接口
 * 用于简化前端支付流程
 */
public interface IPaymentService {

    /**
     * 准备开始支付<br/>
     * 该操作会创建一条支付记录
     *
     * @param orderType 订单类型
     * @param orderSn   订单编号
     * @return ？
     */
    public void ready(String orderType, String orderSn);

    /**
     * 确认提交支付
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param paymentConfigId 支付产品ID
     * @return SubmitPaymentDTO
     */
    public SubmitPaymentDTO submit(String orderType, String orderSn, Long paymentConfigId);

    public boolean paynotify(String sn);

}
