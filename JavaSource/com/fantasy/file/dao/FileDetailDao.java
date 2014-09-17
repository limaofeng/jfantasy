package com.fantasy.file.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FileDetailDao extends HibernateDao<FileDetail, FileDetailKey> {
}