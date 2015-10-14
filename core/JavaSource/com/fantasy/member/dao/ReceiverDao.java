package com.fantasy.member.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.member.bean.Receiver;

@Repository
public class ReceiverDao extends HibernateDao<Receiver,Long> {

}
