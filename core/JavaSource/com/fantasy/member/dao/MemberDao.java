package com.fantasy.member.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.member.bean.Member;

@Repository
public class MemberDao extends HibernateDao<Member, Long> {

}
