package com.fantasy.security.web;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Role;
import com.fantasy.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RestRoleAction extends ActionSupport{

    @Autowired
    private RoleService roleService;

    public String search(Pager<Role> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.roleService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(Role role){
        this.roleService.save(role);
        return SUCCESS;
    }

    public String update(Role role){
        this.roleService.save(role);
        return SUCCESS;
    }

    public String view(String id){
        this.attrs.put(ROOT,this.roleService.get(id));
        return SUCCESS;
    }

    public String delete(String...id){
        this.roleService.delete(id);
        return SUCCESS;
    }
}
