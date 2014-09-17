package com.fantasy.file.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.file.bean.Directory;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class DirectoryDao extends HibernateDao<Directory, String> {

}
