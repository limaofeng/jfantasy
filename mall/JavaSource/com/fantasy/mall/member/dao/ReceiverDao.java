package com.fantasy.mall.member.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.member.bean.Receiver;

@Repository
public class ReceiverDao extends HibernateDao<Receiver,Long> {

}
