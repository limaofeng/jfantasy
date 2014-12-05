package com.fantasy.mall.order.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.bean.Order.PaymentStatus;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao extends HibernateDao<Order, Long> {

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_status");
        Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
        if (filter != null) {
            if ("1".equals(filter.getPropertyValue(String.class))) {//未付款的订单(先付款未付款的)
                criterions = ObjectUtil.join(criterions, Restrictions.eq("orderStatus", Order.OrderStatus.unprocessed), Restrictions.eq("deliveryType.method", DeliveryType.DeliveryMethod.deliveryAgainstPayment), Restrictions.eq("paymentStatus", PaymentStatus.unpaid));
            } else if ("2".equals(filter.getPropertyValue(String.class))) {//未处理，且在线付款且已付款的订单 与 货到付款
                criterions = ObjectUtil.join(criterions, Restrictions.eq("orderStatus", Order.OrderStatus.unprocessed));
                criterions = ObjectUtil.join(criterions, Restrictions.or(Restrictions.and(Restrictions.eq("deliveryType.method", DeliveryType.DeliveryMethod.deliveryAgainstPayment), Restrictions.eq("paymentStatus", PaymentStatus.paid)), Restrictions.eq("deliveryType.method", DeliveryType.DeliveryMethod.cashOnDelivery)));
            } else if ("3".equals(filter.getPropertyValue(String.class))) {//待发货订单 部分发货或者未发货的订单
                criterions = ObjectUtil.join(criterions, Restrictions.eq("orderStatus", Order.OrderStatus.processed));
                criterions = ObjectUtil.join(criterions, Restrictions.or(Restrictions.eq("shippingStatus", Order.ShippingStatus.unshipped), Restrictions.eq("shippingStatus", Order.ShippingStatus.partShipped)));
            }
        }
        return criterions;
    }

}
