package org.jfantasy.common.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.common.bean.HotKeywords;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class HotKeywordsDao extends HibernateDao<HotKeywords, Long> {

}
