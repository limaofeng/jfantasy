package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Invoice;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDao extends HibernateDao<Invoice, Long> {
}
