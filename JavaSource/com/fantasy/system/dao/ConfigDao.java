package com.fantasy.system.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.system.bean.Config;
import com.fantasy.system.bean.ConfigKey;

@Repository("fantasy.sys.hibernate.ConfigDao")
public class ConfigDao extends HibernateDao<Config, ConfigKey> {

}
