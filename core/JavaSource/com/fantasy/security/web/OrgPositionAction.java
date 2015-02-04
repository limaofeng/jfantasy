package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.OrgPosition;
import com.fantasy.security.service.OrgPositionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhx on 2015/2/3.
 */
public class OrgPositionAction extends ActionSupport {

    @Resource
    private OrgPositionService orgPositionService;


    public String findPager(){
        this.search(new Pager<OrgPosition>(),new ArrayList<PropertyFilter>());
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return  SUCCESS;
    }

    public String search(Pager<OrgPosition> pager,List<PropertyFilter> filters){
       this.attrs.put(ROOT, this.orgPositionService.findPager(pager, filters));
       return JSONDATA;
    }

    public String save(OrgPosition orgPosition){
        this.orgPositionService.save(orgPosition);
        return JSONDATA;
    }

    public String view(String id){
       this.attrs.put("orgPosition", this.orgPositionService.get(id));
        return SUCCESS;
    }

    public String delete(String id){
        this.orgPositionService.delete(id);
        return JSONDATA;
    }


}
