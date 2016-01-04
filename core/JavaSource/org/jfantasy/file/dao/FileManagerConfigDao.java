package org.jfantasy.file.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.file.bean.FileManagerConfig;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileManagerConfigDao extends HibernateDao<FileManagerConfig, String>{

}
