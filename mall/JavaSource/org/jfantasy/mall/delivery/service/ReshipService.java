package org.jfantasy.mall.delivery.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.delivery.bean.Reship;
import org.jfantasy.mall.delivery.dao.ReshipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReshipService {

    @Autowired
    private ReshipDao reshipDao;

    public Pager<Reship> findPager(Pager<Reship> pager, List<PropertyFilter> filters) {
        return this.reshipDao.findPager(pager, filters);
    }

    public Reship get(Long id){
        return  this.reshipDao.get(id);
    }

    public void delete(Long[] ids){
        for(Long id:ids){
            this.reshipDao.delete(id);
        }
    }
}
