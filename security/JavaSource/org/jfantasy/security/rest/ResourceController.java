package org.jfantasy.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Permission;
import org.jfantasy.security.bean.Resource;
import org.jfantasy.security.service.PermissionService;
import org.jfantasy.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "security-resources", description = "资源")
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private transient ResourceService resourceService;
    @Autowired
    private transient PermissionService permissionService;

    @ApiOperation(value = "查询资源", notes = "筛选资源，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Resource> search(@ApiParam(value = "分页对象", name = "pager") Pager<Resource> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.resourceService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取单个资源信息", notes = "通过该接口, 获取资源信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Resource view(@PathVariable("id") Long id) {
        return resourceService.get(id);
    }

    @ApiOperation(value = "删除资源", notes = "删除资源时,对应的权限配置也会被删除")
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.resourceService.delete(id);
    }

    @ApiOperation(value = "批量删除资源", notes = "删除资源时,对应的权限配置也会被删除")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.resourceService.delete(id);
    }

    @ApiOperation(value = "添加资源", notes = "通过该接口, 添加资源")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Resource create(@RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    @ApiOperation(value = "更新资源", notes = "通过该接口, 更新资源信息")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Resource update(@PathVariable("id") Long id, @RequestBody Resource resource) {
        resource.setId(id);
        return resourceService.save(resource);
    }

    @ApiOperation(value = "返回资源权限", notes = "返回资源对应的操作权限", response = Permission[].class)
    @RequestMapping(value = "/{id}/permissions", method = {RequestMethod.GET})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("id") Long id) {
        return permissionService.find(Restrictions.eq("resource.id", id));
    }

}
