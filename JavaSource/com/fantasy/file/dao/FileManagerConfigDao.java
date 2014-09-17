package com.fantasy.file.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileManagerConfigDao extends HibernateDao<FileManagerConfig, String>{

}
