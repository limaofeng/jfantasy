package org.jfantasy.member.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Receiver;

@Repository
public class ReceiverDao extends HibernateDao<Receiver,Long> {

}
