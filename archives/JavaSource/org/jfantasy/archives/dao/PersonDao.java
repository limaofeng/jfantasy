package org.jfantasy.archives.dao;

import org.jfantasy.archives.bean.Person;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao extends HibernateDao<Person,Long> {

}
