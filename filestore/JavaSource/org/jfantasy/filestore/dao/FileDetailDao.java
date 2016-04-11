package org.jfantasy.filestore.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.filestore.bean.FileDetail;
import org.jfantasy.filestore.bean.FileDetailKey;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileDetailDao extends HibernateDao<FileDetail, FileDetailKey> {
}