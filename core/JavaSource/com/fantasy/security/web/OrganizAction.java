package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.service.OrganizationService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 组织机构Action
 *
 */
public class OrganizAction extends ActionSupport{

    @Autowired
    private OrganizationService organizationService;


    public String search(Pager<Organization> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.organizationService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(Organization organization){
        this.attrs.put(ROOT,this.organizationService.save(organization));
        return SUCCESS;
    }

    public String update(Organization organization){
        this.attrs.put(ROOT,this.organizationService.save(organization));
        return SUCCESS;
    }

    public String view(String id){
        this.attrs.put(ROOT,this.organizationService.findUnique(Restrictions.eq("id",id)));
        return SUCCESS;
    }

    public String delete(String...id){
        this.organizationService.delete(id);
        return SUCCESS;
    }


}
