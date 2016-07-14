package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Menu;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hibernate.MenuDao")
public class MenuDao extends HibernateDao<Menu, String>{

}
