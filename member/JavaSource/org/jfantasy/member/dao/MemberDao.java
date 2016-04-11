package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao extends HibernateDao<Member, Long> {

}
