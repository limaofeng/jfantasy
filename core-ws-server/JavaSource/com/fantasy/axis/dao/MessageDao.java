package com.fantasy.axis.dao;


import com.fantasy.axis.bean.Message;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository("axis.MessageDao")
public class MessageDao extends HibernateDao<Message,String> {

}
