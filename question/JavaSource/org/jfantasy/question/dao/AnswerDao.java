package org.jfantasy.question.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.question.bean.Answer;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDao extends HibernateDao<Answer,Long> {
}
