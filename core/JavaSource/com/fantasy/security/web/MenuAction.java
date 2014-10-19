package com.fantasy.security.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Menu;
import com.fantasy.security.service.MenuService;
import org.hibernate.criterion.Criterion;

import javax.annotation.Resource;

public class MenuAction extends ActionSupport {

    private static final long serialVersionUID = -7890092416209211498L;

    @Resource
    private transient MenuService menuService;


    public String index() {
        this.attrs.put("menuTree", this.menuService.list(new Criterion[]{/*Restrictions.like("path", SettingUtil.getRootMenuId() + Menu.PATH_SEPARATOR, MatchMode.START)*/}, "sort", "asc"));
        return SUCCESS;
    }

    public String save(Menu menu) throws Exception {
        menuService.save(menu);
        this.attrs.put(ROOT, menu);
        return JSONDATA;
    }

    public String delete(Long id) throws Exception {
        this.menuService.delete(id);
        return JSONDATA;
    }

}
