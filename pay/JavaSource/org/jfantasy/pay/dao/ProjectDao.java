package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Project;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao extends HibernateDao<Project, String> {

}
