package org.jfantasy.website.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.website.bean.Page;

/**
 *@Author lsz
 *@Date 2014-1-2 上午11:26:49
 *
 */

@Repository
public class PageDao extends HibernateDao<Page, Long> {

}

