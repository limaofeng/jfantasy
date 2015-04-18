package com.fantasy.security.web;

import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.struts2.DynaModelDriven;
import com.fantasy.security.bean.Job;
import com.fantasy.security.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 岗位Action
 */
public class JobAction extends ActionSupport implements DynaModelDriven<Job> {

    @Autowired
    private JobService jobService;

    @Override
    public Job getModel(String methodName,String paramName,Class paramType,Map<String, Object> parameters) {
        if("job".equals(paramName) && Job.class.isAssignableFrom(paramType)){
            return VersionUtil.createDynaBean(Job.class, "Job_v1");
        }
        return null;
    }

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
