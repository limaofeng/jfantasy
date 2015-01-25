package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.service.OrgDimensionService;
import com.fantasy.security.service.OrganizationService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2015/1/21.
 */
public class OrganizAction extends ActionSupport {

    @Resource
    private OrganizationService organizationService;


    public String index(List<PropertyFilter> filters){
        filters.add(new PropertyFilter("EQI_layer","1"));
        this.attrs.put(ROOT, this.organizationService.find(filters));
        return JSONDATA;
    }

    public String search(Pager<Organization> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.organizationService.findPager(pager,filters));
        return JSONDATA;
    }



    public String save(Organization organization){
        this.organizationService.save(organization);
        return JSONDATA;
    }

    public String view(String id){
        this.attrs.put(ROOT,this.organizationService.findUnique(Restrictions.eq("id",id)));
        return JSONDATA;
    }

    public String delete(String...ids){
        this.organizationService.delete(ids);
        return JSONDATA;
    }


}
