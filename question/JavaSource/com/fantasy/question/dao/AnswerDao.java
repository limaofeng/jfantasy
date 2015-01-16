package com.fantasy.question.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.question.bean.Answer;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDao extends HibernateDao<Answer,Long> {
}
