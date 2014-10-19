package com.fantasy.file.dao;

import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.file.bean.FilePart;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class FilePartDao extends HibernateDao<FilePart,FileDetailKey> {

}
