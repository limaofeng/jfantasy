package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Log;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao extends HibernateDao<Log, Long> {

}
