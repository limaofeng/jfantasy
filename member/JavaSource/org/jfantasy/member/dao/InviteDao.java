package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Invite;
import org.springframework.stereotype.Repository;

@Repository
public class InviteDao extends HibernateDao<Invite, Long> {
}
