package com.fantasy.system.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.system.bean.ConfigType;

@Repository("fantasy.sys.hibernate.ConfigTypeDao")
public class ConfigTypeDao extends HibernateDao<ConfigType, String> {
}
