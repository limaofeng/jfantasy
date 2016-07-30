package org.jfantasy.member.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.member.bean.Invoice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceDao extends HibernateDao<Invoice, Long> {

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter orderSn = ObjectUtil.remove(filters, "filterName", "LIKES_orderSn");
        PropertyFilter orderType = ObjectUtil.remove(filters, "filterName", "EQS_orderType");
        Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
        if (orderSn != null) {
            criterions = ObjectUtil.join(criterions, Restrictions.like("items.order.orderSn", orderSn.getPropertyValue()));
        }
        if (orderType != null) {
            criterions = ObjectUtil.join(criterions, Restrictions.like("items.order.orderType", orderType.getPropertyValue()));
        }
        return criterions;
    }

}
