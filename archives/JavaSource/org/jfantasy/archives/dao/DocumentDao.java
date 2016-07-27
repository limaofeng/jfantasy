package org.jfantasy.archives.dao;

import org.jfantasy.archives.bean.Document;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentDao extends HibernateDao<Document, Long> {
}
