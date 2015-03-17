package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Role;
import com.fantasy.security.service.ResourceService;
import com.fantasy.security.service.RoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class RoleAction extends ActionSupport {

    private static final long serialVersionUID = 4738280714802564952L;

    @Autowired(name = "fantasy.auth.RoleService")
    private transient RoleService roleService;
    @Autowired
    private transient ResourceService resourceService;

    /**
     * 角色列表
     *
     * @return {string}
     */
    public String index() {
        this.search(new Pager<Role>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 角色查询
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return {string}
     */
    public String search(Pager<Role> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.roleService.findPager(pager, filters));
        return JSONDATA;
    }

    public String save(Role role) {
        this.roleService.save(role);
        this.attrs.put(ROOT, role);
        return JSONDATA;
    }

    public String edit(String id) {
        this.attrs.put("role", this.roleService.get(id));
        return SUCCESS;
    }

    /**
     * 删除角色
     *
     * @param ids 角色ids
     * @return {string}
     */
    public String delete(String... ids) {
        this.roleService.delete(ids);
        return JSONDATA;
    }

    /**
     * 授权编辑
     *
     * @param id 角色id
     * @return {string}
     */
    public String resourceEdit(String id) {
        this.attrs.put("role", this.roleService.get(id));
        this.attrs.put("allResources", this.resourceService.loadResourcesByUrl());
        return SUCCESS;
    }
}
