package org.jfantasy.cms.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.cms.bean.Topic;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class TopicDao extends HibernateDao<Topic, String>{

}
