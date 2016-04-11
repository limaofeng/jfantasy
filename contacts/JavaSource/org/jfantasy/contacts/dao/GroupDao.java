package org.jfantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.contacts.bean.Group;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.ab.hibernate.GroupDao")
public class GroupDao extends HibernateDao<Group, Long> {

}
