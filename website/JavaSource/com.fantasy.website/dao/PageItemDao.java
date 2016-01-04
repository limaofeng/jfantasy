package org.jfantasy.website.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.website.bean.PageItem;
import org.springframework.stereotype.Repository;

@Repository
public class PageItemDao extends HibernateDao<PageItem, Long> {

}

