package com.fantasy.remind.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.remind.bean.Notice;
import org.springframework.stereotype.Repository;

/**
 * 公告 Dao
 */

@Repository
public class NoticeDao extends HibernateDao<Notice,Long> {
}
