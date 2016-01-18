package org.jfantasy.pay.product;

import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;

/**
 * 支付产品接口
 */
public interface PayProduct {

    //网页支付
    String web(Order order, Payment payment) throws PayException ;

    //WAP支付
    String wap();

    /**
     * app支付
     *
     * @param order   待支付订单
     * @param payment 支付记录
     * @return String
     */
    String app(Order order, Payment payment) throws PayException;

    //异步通知
    String asyncNotify();

    //同步通知
    String syncNotify();


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
     * 收款方账号
     *
     * @return String
     */
    String getShroffAccountName();

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
