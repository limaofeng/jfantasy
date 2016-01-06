package org.jfantasy.file.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileDetailDao extends HibernateDao<FileDetail, FileDetailKey> {
}