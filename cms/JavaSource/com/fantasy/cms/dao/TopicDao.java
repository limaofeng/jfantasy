package com.fantasy.cms.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.cms.bean.Topic;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class TopicDao extends HibernateDao<Topic, String>{

}
