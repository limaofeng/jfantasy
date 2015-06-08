package com.fantasy.swp.service;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Trigger;
import com.fantasy.swp.dao.TriggerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TriggerService {

    @Autowired
    private TriggerDao triggerDao;

    public Pager<Trigger> findPager(Pager<Trigger> pager, List<PropertyFilter> filters) {
        return this.triggerDao.findPager(pager, filters);
    }

    public Trigger save(Trigger trigger) {
        this.triggerDao.save(trigger);
        return trigger;
    }

    public Trigger get(Long id) {
        return this.triggerDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.triggerDao.delete(id);
        }
    }


    public List<Trigger> find(List<PropertyFilter> filters){
        return this.triggerDao.find(filters);
    }
}
