package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.service.OrgDimensionService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 组织维度Action
 */
public class OrgDimensionAction extends ActionSupport {

    @Autowired
    private OrgDimensionService orgDimensionService;

    public String search(Pager<OrgDimension> pager,List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.orgDimensionService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(OrgDimension orgDimension) {
        this.orgDimensionService.save(orgDimension);
        return SUCCESS;
    }

    public String update(OrgDimension orgDimension) {
        this.orgDimensionService.save(orgDimension);
        return SUCCESS;
    }

    public String view(String id) {
        this.attrs.put(ROOT, this.orgDimensionService.findUnique(Restrictions.eq("id", id)));
        return SUCCESS;
    }

    public String delete(String... id) {
        this.orgDimensionService.delete(id);
        return SUCCESS;
    }


}
