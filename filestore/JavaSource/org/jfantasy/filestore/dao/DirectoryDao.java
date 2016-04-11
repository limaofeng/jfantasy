package org.jfantasy.filestore.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.filestore.bean.Directory;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class DirectoryDao extends HibernateDao<Directory, String> {

}
