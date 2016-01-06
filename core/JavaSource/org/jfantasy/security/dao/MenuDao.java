package org.jfantasy.security.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Menu;

@Repository("fantasy.auth.hibernate.MenuDao")
public class MenuDao extends HibernateDao<Menu, Long>{

}
