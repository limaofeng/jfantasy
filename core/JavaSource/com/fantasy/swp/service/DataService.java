package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.dao.DataDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

