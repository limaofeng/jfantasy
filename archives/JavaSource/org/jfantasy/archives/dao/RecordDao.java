package org.jfantasy.archives.dao;

import org.jfantasy.archives.bean.Record;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class RecordDao extends HibernateDao<Record, Long> {
}
