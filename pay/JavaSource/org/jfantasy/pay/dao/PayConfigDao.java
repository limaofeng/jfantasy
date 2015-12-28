package org.jfantasy.pay.dao;

import org.jfantasy.pay.bean.PayConfig;
import org.springframework.stereotype.Repository;
import com.fantasy.framework.dao.hibernate.HibernateDao;


@Repository
public class PayConfigDao extends HibernateDao<PayConfig, Long> {

}

