package org.jfantasy.website.service;


import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.website.bean.Trigger;
import org.jfantasy.website.dao.TriggerDao;
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
