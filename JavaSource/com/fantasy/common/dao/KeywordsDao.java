package com.fantasy.common.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.common.bean.Keywords;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class KeywordsDao extends HibernateDao<Keywords, Long> {

}
