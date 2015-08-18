package com.fantasy.website.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.website.bean.Page;

/**
 *@Author lsz
 *@Date 2014-1-2 上午11:26:49
 *
 */

@Repository
public class PageDao extends HibernateDao<Page, Long> {

}

