package org.jfantasy.system.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.system.bean.Website;
import org.springframework.stereotype.Repository;

@Repository
public class WebsiteDao extends HibernateDao<Website, Long>{

}
