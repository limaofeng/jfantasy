package org.jfantasy.pay.order;


import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.PaymentService;
import org.jfantasy.rpc.annotation.ServiceExporter;
import org.jfantasy.rpc.exception.RpcException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@ServiceExporter(targetInterface = OrderProcessor.class)
public class OrderProcessorImpl implements OrderProcessor {

    @Autowired
    private PayService payService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public RefundDetails refund(OrderKey key, BigDecimal amount, String remark) {
        List<Payment> payments = paymentService.find(Restrictions.eq("order.sn", key.getSn()), Restrictions.eq("order.type", key.getType()));
        Payment payment = ObjectUtil.find(payments, "status", PaymentStatus.success);
        if (payment == null) {
            throw new RpcException(" 订单可能未支付成功或者已经退款! ");
        }
        return BeanUtil.copyProperties(new RefundDetails(), payService.refund(payment.getSn(), amount, remark));
    }

}
