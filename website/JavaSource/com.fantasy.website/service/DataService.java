package org.jfantasy.website.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.website.bean.Data;
import org.jfantasy.website.dao.DataDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class DataService {

    @Autowired
    private DataDao dataDao;

    public Pager<Data> findPager(Pager<Data> pager, List<PropertyFilter> filters) {
        return this.dataDao.findPager(pager, filters);
    }

    public void save(Data data) {
        this.dataDao.save(data);
    }

    public Data getData(Long id) {
        return this.dataDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.dataDao.delete(id);
        }
    }

    public List<Data> find(List<PropertyFilter> filters) {
        return this.dataDao.find(filters);
    }
}

