package com.fantasy.website.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.website.bean.PageItem;
import com.fantasy.website.dao.PageItemDao;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class PageItemService {

    @Autowired
    private PageItemDao pageItemDao;

    public Pager<PageItem> findPager(Pager<PageItem> pager, List<PropertyFilter> filters) {
        return this.pageItemDao.findPager(pager, filters);
    }

    public void save(PageItem pageItem) {
        this.pageItemDao.save(pageItem);
    }

    public PageItem getData(Long id) {
        return this.pageItemDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.delete(id);
        }
    }

    public void delete(Long id){
        this.pageItemDao.delete(id);
    }

    public List<PageItem> find(List<PropertyFilter> filters) {
        return this.pageItemDao.find(filters);
    }

    public PageItem get(Long id) {
        PageItem pageItem = this.pageItemDao.get(id);
        if(pageItem!=null){
            Hibernate.initialize(pageItem.getPage().getTemplate().getDataInferfaces());
            Hibernate.initialize(pageItem.getPage().getWebSite().getDefaultFileManager());
            Hibernate.initialize(pageItem.getPage().getDatas());
        }
        return pageItem;
    }

    public PageItem findUniqueByPath(String path, Long pageId) {
        PageItem pageItem = this.pageItemDao.findUnique(Restrictions.eq("file", path),Restrictions.eq("page.id", pageId));
        if(pageItem!=null){
            Hibernate.initialize(pageItem.getPage().getTemplate().getDataInferfaces());
            Hibernate.initialize(pageItem.getPage().getWebSite().getDefaultFileManager());
            Hibernate.initialize(pageItem.getPage().getDatas());
        }
        return pageItem;
    }
}

