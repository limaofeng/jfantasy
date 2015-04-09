package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserAction extends ActionSupport {

    @Autowired
    private UserService userService;

    public String search(Pager<User> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.userService.findPager(pager,filters));
        return SUCCESS;
    }

    public String create(User user){
       this.userService.save(user);
        return SUCCESS;
    }

    public String update(User user){
        this.userService.save(user);
        return SUCCESS;
    }

    public String view(String id){
        this.attrs.put(ROOT,this.userService.findUniqueByUsername(id));
        return SUCCESS;
    }

    public String delete(Long...id){
        this.userService.delete(id);
        return SUCCESS;
    }
}
