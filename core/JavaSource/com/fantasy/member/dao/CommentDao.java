package com.fantasy.member.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.member.bean.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends HibernateDao<Comment,Long> {
}
