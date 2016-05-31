package org.jfantasy.oauth.dao;

import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class ApiKeyDao extends HibernateDao<ApiKey, String> {
}
