package com.fantasy.security.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Permission;
import com.fantasy.security.bean.Role;
import com.fantasy.security.service.PermissionService;
import com.fantasy.security.service.RoleService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security/roles")
public class RoleController {

    @Autowired
    private transient RoleService roleService;
    @Autowired
    private transient PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Role> search(Pager<Role> pager, List<PropertyFilter> filters) {
        return this.roleService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{code}", method = {RequestMethod.GET})
    @ResponseBody
    public Role view(@PathVariable("code") String code) {
        return roleService.get(code);
    }

    @RequestMapping(value = "/{code}/permissions", method = {RequestMethod.GET})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("code") String code) {
        return permissionService.find(Restrictions.eq("roles.code", code));
    }

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Role create(@RequestBody Role role) {
        return roleService.save(role);
    }

    @RequestMapping(value = "/{code}", method = {RequestMethod.PUT})
    @ResponseBody
    public Role update(@PathVariable("code") String code, @RequestBody Role role) {
        role.setCode(code);
        return roleService.save(role);
    }

    @RequestMapping(value = "/{code}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("code") String code) {
        this.roleService.delete(code);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... codes) {
        this.roleService.delete(codes);
    }

}
