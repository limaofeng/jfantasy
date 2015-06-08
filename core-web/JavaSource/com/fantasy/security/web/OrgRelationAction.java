package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.OrgRelation;
import com.fantasy.security.service.OrgRelationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrgRelationAction extends ActionSupport {

    @Autowired
    private OrgRelationService orgRelationService;

    public String search(Pager<OrgRelation> pager,List<PropertyFilter> filters){
       this.attrs.put(ROOT,this.orgRelationService.findPager(pager, filters));
        return SUCCESS;
    }
}
