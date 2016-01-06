package org.jfantasy.website.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.website.bean.Page;
import org.springframework.stereotype.Repository;

/**
 *@Author lsz
 *@Date 2014-1-2 上午11:26:49
 *
 */

@Repository
public class PageDao extends HibernateDao<Page, Long> {

}

