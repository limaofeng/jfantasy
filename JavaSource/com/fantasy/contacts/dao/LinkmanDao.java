package com.fantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.contacts.bean.Linkman;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.ab.hibernate.LinkmanDao")
public class LinkmanDao extends HibernateDao<Linkman, Long>{

}
