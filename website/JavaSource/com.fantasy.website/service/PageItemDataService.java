package com.fantasy.website.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.website.bean.PageItemData;
import com.fantasy.website.dao.PageItemDataDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

