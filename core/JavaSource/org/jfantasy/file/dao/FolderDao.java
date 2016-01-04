package org.jfantasy.file.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.file.bean.Folder;
import org.jfantasy.file.bean.FolderKey;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FolderDao extends HibernateDao<Folder, FolderKey> {

}