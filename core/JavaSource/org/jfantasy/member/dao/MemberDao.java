package org.jfantasy.member.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Member;

@Repository
public class MemberDao extends HibernateDao<Member, Long> {

}
