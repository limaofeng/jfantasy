package org.jfantasy.archives.service;

import org.jfantasy.archives.bean.Document;
import org.jfantasy.archives.dao.DocumentDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentDao documentDao;

    public Pager<Document> findPager(Pager<Document> pager, List<PropertyFilter> filters) {
        return this.documentDao.findPager(pager, filters);
    }

    public Document save(Document document) {
        return this.documentDao.save(document);
    }

    public Document update(Document document, boolean has) {
        return this.documentDao.update(document, has);
    }

    public void deltele(Long... ids) {
        for (Long id : ids) {
            this.documentDao.delete(id);
        }
    }

    public Document get(Long id) {
        return this.documentDao.get(id);
    }

}
