package com.fantasy.security.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Permission;
import com.fantasy.security.service.PermissionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "security-permissions", description = "网站权限")
@RestController
@RequestMapping("/security/permissions")
public class PermissionController {

    @Autowired
    private transient PermissionService permissionService;

    @ApiOperation(value = "查询权限", notes = "筛选权限，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Permission> search(@ApiParam(value = "分页对象", name = "pager") Pager<Permission> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.permissionService.findPager(pager, filters);
    }

    @ApiOperation(value = "删除权限", notes = "通过权限ID,删除权限")
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.permissionService.delete(id);
    }

    @ApiOperation(value = "批量删除权限", notes = "通过权限ID,删除权限")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.permissionService.delete(id);
    }

    @ApiOperation(value = "添加权限", notes = "添加权限")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Permission create(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    @ApiOperation(value = "更新权限", notes = "更新权限")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Permission update(@PathVariable("id") Long id, @RequestBody Permission permission) {
        permission.setId(id);
        return permissionService.save(permission);
    }

}
