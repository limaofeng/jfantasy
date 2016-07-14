package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Point;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao extends HibernateDao<Point, Long> {
}
