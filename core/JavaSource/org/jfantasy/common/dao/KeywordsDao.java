package org.jfantasy.common.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.common.bean.Keywords;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class KeywordsDao extends HibernateDao<Keywords, Long> {

}
