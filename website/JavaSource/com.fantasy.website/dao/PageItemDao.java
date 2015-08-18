package com.fantasy.website.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.website.bean.PageItem;
import org.springframework.stereotype.Repository;

@Repository
public class PageItemDao extends HibernateDao<PageItem, Long> {

}

