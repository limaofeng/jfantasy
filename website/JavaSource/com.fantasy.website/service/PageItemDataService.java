package org.jfantasy.website.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.website.bean.PageItemData;
import org.jfantasy.website.dao.PageItemDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PageItemDataService {

    @Autowired
    private PageItemDataDao pageItemDataDao;

    public Pager<PageItemData> findPager(Pager<PageItemData> pager, List<PropertyFilter> filters) {
        return this.pageItemDataDao.findPager(pager, filters);
    }

    public void save(PageItemData data) {
        this.pageItemDataDao.save(data);
    }

    public PageItemData getData(Long id) {
        return this.pageItemDataDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pageItemDataDao.delete(id);
        }
    }

    public List<PageItemData> find(List<PropertyFilter> filters) {
        return this.pageItemDataDao.find(filters);
    }
}

