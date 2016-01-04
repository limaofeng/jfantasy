package org.jfantasy.website.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.website.bean.DataInferface;
import org.jfantasy.website.dao.DataInferfaceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service
@Transactional
public class DataInferfaceService {

    @Autowired
    private DataInferfaceDao dataInferfaceDao;


    public Pager<DataInferface> findPager(Pager<DataInferface> pager, List<PropertyFilter> filters) {
        return this.dataInferfaceDao.findPager(pager, filters);
    }

    public void save(DataInferface face) {
        this.dataInferfaceDao.save(face);
    }

    public DataInferface getDataInferface(Long id) {
        return this.dataInferfaceDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.dataInferfaceDao.delete(id);
        }
    }

    public List<DataInferface> find(List<PropertyFilter> filters) {
        return this.dataInferfaceDao.find(filters);
    }
}

