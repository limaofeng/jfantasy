package com.fantasy.payment.ws;

import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.payment.ws.dto.PaymentConfigDTO;
import com.fantasy.payment.ws.dto.PaymentDTO;

import java.util.Map;

/**
 * 支付 webservice 接口
 * 用于简化前端支付流程
 */
public interface IPaymentService {

    /**
     * 获取支付配置
     *
     * @param filters 筛选条件
     * @return PaymentConfigDTO[]
     */
    public PaymentConfigDTO[] find(PropertyFilterDTO[] filters);

    /**
     * 创建请求表单
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param paymentConfigId 支付配置
     * @param payMember       支付人
     * @param parameters      请求参数
     * @return String
     */
    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, String payMember, Map<String, String> parameters);

    /**
     * 同步通知接口
     *
     * @param sn         编号
     * @param parameters 请求参数
     * @return String
     */
    public String payreturn(String sn, Map<String, String> parameters);

    /**
     * 异步通知接口
     *
     * @param sn         编号
     * @param parameters 请求参数
     * @return String
     */
    public String paynotify(String sn, Map<String, String> parameters);

    /**
     * 获取支付信息
     *
     * @param sn 编号
     * @return PaymentDTO
     */
    public PaymentDTO getPayment(String sn);

}
