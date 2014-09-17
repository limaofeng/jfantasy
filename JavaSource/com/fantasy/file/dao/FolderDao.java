package com.fantasy.file.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.file.bean.Folder;
import com.fantasy.file.bean.FolderKey;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FolderDao extends HibernateDao<Folder, FolderKey> {

}