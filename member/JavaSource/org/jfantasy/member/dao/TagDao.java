package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao extends HibernateDao<Tag, Long> {
}
