package com.fantasy.security.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Menu;

@Repository("fantasy.auth.hibernate.MenuDao")
public class MenuDao extends HibernateDao<Menu, Long>{

}
