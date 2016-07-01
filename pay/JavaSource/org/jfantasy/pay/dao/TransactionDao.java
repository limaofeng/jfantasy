package org.jfantasy.pay.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDao extends HibernateDao<Transaction, String> {

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_account.sn");
        Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
        if (filter != null) {
            String accountSn = filter.getPropertyValue(String.class);
            criterions = ObjectUtil.join(criterions, Restrictions.or(Restrictions.eq("from", accountSn), Restrictions.eq("to", accountSn)));
        }
        return criterions;
    }

}
