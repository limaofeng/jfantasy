package org.jfantasy.file.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.file.bean.Directory;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class DirectoryDao extends HibernateDao<Directory, String> {

}
