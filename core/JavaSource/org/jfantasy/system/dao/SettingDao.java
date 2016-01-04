package org.jfantasy.system.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.system.bean.Setting;
import org.springframework.stereotype.Repository;

@Repository
public class SettingDao extends HibernateDao<Setting, Long>{

}
