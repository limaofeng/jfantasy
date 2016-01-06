package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends HibernateDao<Comment,Long> {
}
