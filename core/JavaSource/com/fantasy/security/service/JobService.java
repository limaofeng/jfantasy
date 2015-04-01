package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Job;
import com.fantasy.security.dao.JobDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by yhx on 2015/2/4.
 */
@Service
@Transactional
public class JobService  {

    @Autowired
    private JobDao jobDao;


    public Pager<Job> findPager(Pager<Job> pager,List<PropertyFilter> filters){
        return this.jobDao.findPager(pager,filters);
    }


    public List<Job> find(Criterion...criterions){
        return this.jobDao.find(criterions);
    }


    public Job save(Job job){
        this.jobDao.save(job);
        return job;
    }


    public Job get(Long id){
        return  this.jobDao.get(id);
    }

    public void delete(Long...ids){
        for(Long id:ids){
            this.jobDao.delete(id);
        }
    }

    public Job findUnique(String code){
        return this.jobDao.findUnique(Restrictions.eq("code",code));
    }


}
