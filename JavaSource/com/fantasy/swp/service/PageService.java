package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.dao.PageDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class PageService{

    @Resource
    private PageDao pageDao;



    public Pager<Page> findPager(Pager<Page> pager, List<PropertyFilter> filters) {
        return this.pageDao.findPager(pager, filters);
    }

    public void save(Page page) {
        this.pageDao.save(page);
    }

    public Page get(Long id) {
        return this.pageDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pageDao.delete(id);
        }
    }

    public void generation(Long id) {
    }

}

