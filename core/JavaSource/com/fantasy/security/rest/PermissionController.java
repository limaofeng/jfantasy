package com.fantasy.security.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Permission;
import com.fantasy.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security/permissions")
public class PermissionController {

    @Autowired
    private transient PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Permission> search(Pager<Permission> pager, List<PropertyFilter> filters) {
        return this.permissionService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.permissionService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.permissionService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Permission create(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Permission update(@PathVariable("id") Long id, @RequestBody Permission permission) {
        permission.setId(id);
        return permissionService.save(permission);
    }

}
