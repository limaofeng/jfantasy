package org.jfantasy.website.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.website.bean.Template;

/**
 *@Author lsz
 *@Date 2014-1-2 下午4:48:25
 *
 */
@Repository
public class TemplateDao extends HibernateDao<Template, Long> {

}

