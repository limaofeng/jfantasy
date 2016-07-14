package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Point;
import org.springframework.stereotype.Repository;

@Repository("mem.PointDao")
public class PointDao extends HibernateDao<Point, Long>{

}
