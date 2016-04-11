package org.jfantasy.filestore.dao;

import org.jfantasy.filestore.bean.FileDetailKey;
import org.jfantasy.filestore.bean.FilePart;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class FilePartDao extends HibernateDao<FilePart,FileDetailKey> {

}
