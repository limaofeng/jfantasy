package org.jfantasy.system.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.system.bean.Website;

@Repository
public class WebsiteDao extends HibernateDao<Website, Long>{

}
