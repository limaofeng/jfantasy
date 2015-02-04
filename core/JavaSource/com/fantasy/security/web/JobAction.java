package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Job;
import com.fantasy.security.service.JobService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2015/2/4.
 */
public class JobAction extends ActionSupport {

    @Resource
    private JobService jobService;


    public String index(Pager<Job> pager,List<PropertyFilter> filters){
        this.search(pager,filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Job> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.jobService.findPager(pager,filters));
        return JSONDATA;
    }

    public String save(Job job){
        this.jobService.save(job);
        return JSONDATA;
    }

    public String view(Long id){
        this.attrs.put("job",this.jobService.get(id));
        return SUCCESS;
    }

    public String delete(Long...ids){
        this.jobService.delete(ids);
        return JSONDATA;
    }


}
