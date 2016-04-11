package org.jfantasy.filestore.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.filestore.bean.FileManagerConfig;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileManagerConfigDao extends HibernateDao<FileManagerConfig, String>{

}
