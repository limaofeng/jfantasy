package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Job;
import com.fantasy.security.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 岗位Action
 */
public class JobAction extends ActionSupport {

    @Autowired
    private JobService jobService;


    public String search(Pager<Job> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.jobService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(Job job){
        this.attrs.put(ROOT, this.jobService.save(job));
        return SUCCESS;
    }

    public String update(Job job){
        this.attrs.put(ROOT, this.jobService.save(job));
        return SUCCESS;
    }

    public String view(Long id){
        this.attrs.put(ROOT,this.jobService.get(id));
        return SUCCESS;
    }

    public String delete(Long...id){
        this.jobService.delete(id);
        return SUCCESS;
    }


}
