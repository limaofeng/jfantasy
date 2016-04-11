package org.jfantasy.filestore.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.filestore.bean.Folder;
import org.jfantasy.filestore.bean.FolderKey;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FolderDao extends HibernateDao<Folder, FolderKey> {

}