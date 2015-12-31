package org.jfantasy.express.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.express.bean.Express;
import org.springframework.stereotype.Repository;

@Repository
public class ExpressDao extends HibernateDao<Express,String> {
}
