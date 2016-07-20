package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Receiver;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiverDao extends HibernateDao<Receiver,Long> {
}
