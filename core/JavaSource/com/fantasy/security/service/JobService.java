package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Job;
import com.fantasy.security.dao.JobDao;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2015/2/4.
 */
@Service
@Transactional
public class JobService  {

    @Resource
    private JobDao jobDao;


    public Pager<Job> findPager(Pager<Job> pager,List<PropertyFilter> filters){
        return this.jobDao.findPager(pager,filters);
    }


    public List<Job> find(Criterion...criterions){
        return this.jobDao.find(criterions);
    }


    public void save(Job job){
        this.jobDao.save(job);
    }


    public Job get(Long id){
        return  this.jobDao.get(id);
    }

    public void delete(Long...ids){
        for(Long id:ids){
            this.jobDao.delete(id);
        }
    }


}
