package com.fantasy.mall.delivery.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.Reship;
import com.fantasy.mall.delivery.dao.ReshipDao;
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
