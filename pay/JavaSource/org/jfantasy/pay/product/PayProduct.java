package org.jfantasy.pay.product;

import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;

import java.util.Properties;

/**
 * 支付产品接口
 */
public interface PayProduct {

    /**
     * 网页支付
     *
     * @param order      订单对象
     * @param payment    支付记录
     * @param properties 额外属性(一般由调用端自定义)
     * @return String
     * @throws PayException 支付异常
     */
    Object web(Payment payment, Order order, Properties properties) throws PayException;

    /**
     * app支付
     *
     * @param order   待支付订单
     * @param payment 支付记录
     * @return String
     */
    Object app(Payment payment, Order order, Properties properties) throws PayException;

    /**
     * 支付通知
     *
     * @param payment 付款对象
     * @param result  通知内容
     * @return Payment
     * @throws PayException 支付异常
     */
    Object payNotify(Payment payment, String result) throws PayException;

    /**
     * 退款成功通知
     *
     * @param refund 退款对象
     * @param result 通知内容
     * @return Refund
     * @throws PayException 支付异常
     */
    Object payNotify(Refund refund, String result) throws PayException;

    /**
     * 退款操作
     *
     * @param refund 退款对象
     * @return String
     */
    String refund(Refund refund);

    /**
     * 查询支付状态
     *
     * @param payment 支付对象
     * @return PaymentStatus
     */
    PaymentStatus query(Payment payment) throws PayException;

    /**
     * 关闭交易
     *
     * @param payment 支付对象
     */
    void close(Payment payment) throws PayException;

    /*----------以下非业务方法---------------*/

    /**
     * 支付产品标示
     *
     * @return String
     */
    String getId();

    /**
     * 支付产品名称
     *
     * @return String
     */
    String getName();

    /**
     * 商户ID参数名称
     *
     * @return String
     */
    String getBargainorIdName();

    /**
     * 密钥参数名称
     *
     * @return String
     */
    String getBargainorKeyName();

    /**
     * 支付产品描述
     *
     * @return String
     */
    String getDescription();

    /**
     * 支付产品LOGO路径
     *
     * @return String
     */
    String getLogoPath();

    /**
     * 支持货币类型
     *
     * @return CurrencyType[]
     */
    CurrencyType[] getCurrencyTypes();
}
