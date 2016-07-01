package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.WalletBill;
import org.springframework.stereotype.Repository;

@Repository
public class WalletBillDao extends HibernateDao<WalletBill, Long> {
}
