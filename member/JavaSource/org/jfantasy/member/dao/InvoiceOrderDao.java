package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.InvoiceOrder;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceOrderDao extends HibernateDao<InvoiceOrder, Long> {
}
