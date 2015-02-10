package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.PageItem;
import com.fantasy.swp.dao.PageItemDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class PageItemService {

    @Resource
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
            this.pageItemDao.delete(id);
        }
    }

    public List<PageItem> find(List<PropertyFilter> filters) {
        return this.pageItemDao.find(filters);
    }

    public PageItem get(Long id) {
        return this.pageItemDao.get(id);
    }
}

