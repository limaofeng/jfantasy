package com.fantasy.system.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.system.bean.Website;

@Repository
public class WebsiteDao extends HibernateDao<Website, Long>{

}
