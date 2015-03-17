package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.dao.DataInferfaceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

