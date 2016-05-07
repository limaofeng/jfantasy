package org.jfantasy.common.dao;

import org.jfantasy.common.bean.Keyword;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class KeywordDao extends HibernateDao<Keyword,Long>{
}
