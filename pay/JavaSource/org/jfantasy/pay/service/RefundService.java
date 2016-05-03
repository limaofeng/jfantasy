package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.SpELUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.dao.OrderDao;
import org.jfantasy.pay.dao.PaymentDao;
import org.jfantasy.pay.dao.RefundDao;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class RefundService {

    @Autowired
    private RefundDao refundDao;
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private OrderDao orderDao;

    public Refund get(String sn) {
        return this.refundDao.get(sn);
    }

    public void result(Refund refund, Order order) {
        refund.setOrder(order);
        this.refundDao.save(refund);
    }

    public Refund ready(OrderKey key, BigDecimal amount, String remark) {
        Payment payment = this.paymentDao.findUnique(Restrictions.eq("order.sn",key.getSn()),Restrictions.eq("order.type",key.getType()),Restrictions.eq("status", PaymentStatus.success));
        return ready(payment,amount,remark);
    }



    /**
     * 退款准备
     *
     * @param payment 原支付交易
     * @param amount  退款金额
     * @param remark  备注
     * @return Refund
     */
    public Refund ready(Payment payment, final BigDecimal amount, String remark) {
        if (payment.getStatus() != PaymentStatus.success) {
            throw new PayException("原交易[" + payment.getSn() + "]未支付成功,不能发起退款操作");
        }
        List<Refund> refunds = this.refundDao.find(Restrictions.eq("payment.sn", payment.getSn()));
        Refund refund = ObjectUtil.find(refunds, "status", RefundStatus.wait);
        try {
            if (refund != null) {//存在等待中的退单
                return refund;
            }
            refund = ObjectUtil.find(refunds, SpELUtil.getExpression(" #status == #value.status and #amount.equals(#value.amount) "), new HashMap<String, Object>() {
                {
                    this.put("status", RefundStatus.ready);
                    this.put("amount", amount);
                }
            });//存在相同的退单
            if (refund != null) {
                return refund;
            }
            BigDecimal totalRefunded = BigDecimal.ZERO;
            for (Refund _refund : ObjectUtil.filter(refunds, "status", RefundStatus.success)) {
                totalRefunded = totalRefunded.add(_refund.getTotalAmount());
            }
            totalRefunded = totalRefunded.add(amount);
            if (totalRefunded.compareTo(payment.getTotalAmount()) > 0) {
                throw new PayException("退款金额[" + totalRefunded + "]已经大于付款金额[" + payment.getTotalAmount() + "],不能执行退款操作");
            }
            refund = new Refund(payment);
            refund.setTotalAmount(amount);
            refund.setMemo(remark);
            return refund = this.refundDao.save(refund);
        } finally {
            for (Refund _refund : ObjectUtil.filter(refunds, "status", RefundStatus.ready)) {//将其余订单设置为失败
                assert refund != null;
                if (_refund.getSn().equals(refund.getSn())) {
                    continue;
                }
                _refund.setStatus(RefundStatus.failure);
                this.refundDao.save(_refund);
            }
        }
    }

    public Refund save(Refund refund) {
        return this.refundDao.save(refund);
    }

    public Pager<Refund> findPager(Pager<Refund> pager, List<PropertyFilter> filters) {
        return this.refundDao.findPager(pager, filters);
    }

}
