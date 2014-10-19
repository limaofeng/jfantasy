package com.fantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.contacts.bean.Group;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.ab.hibernate.GroupDao")
public class GroupDao extends HibernateDao<Group, Long> {

}
