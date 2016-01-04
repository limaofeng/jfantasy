package org.jfantasy.file.dao;

import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.file.bean.FilePart;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class FilePartDao extends HibernateDao<FilePart,FileDetailKey> {

}
