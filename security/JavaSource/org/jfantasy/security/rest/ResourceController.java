package org.jfantasy.security.rest;

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

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private transient ResourceService resourceService;
    @Autowired
    private transient PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Resource> search(Pager<Resource> pager, List<PropertyFilter> filters) {
        return this.resourceService.findPager(pager, filters);
    }

    /**
     * 获取单个资源信息 <br/>
     * 通过该接口, 获取资源信息
     * @param id id
     * @return Resource
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Resource view(@PathVariable("id") Long id) {
        return resourceService.get(id);
    }

    /**
     * 删除资源<br/>
     * 删除资源时,对应的权限配置也会被删除
     * @param id id
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.resourceService.delete(id);
    }

    /**
     * 批量删除资源 <br/>
     * 删除资源时,对应的权限配置也会被删除
     * @param id ids
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.resourceService.delete(id);
    }

    /**
     * 添加资源 <br/>
     * 通过该接口, 添加资源
     * @param resource resource
     * @return Resource
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Resource create(@RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    /**
     * 更新资源 <br/>
     * 通过该接口, 更新资源信息
     * @param id id
     * @param resource resource
     * @return Resource
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    @ResponseBody
    public Resource update(@PathVariable("id") Long id, @RequestBody Resource resource) {
        resource.setId(id);
        return resourceService.save(resource);
    }

    /**
     * 返回资源权限 <br/>
     * 返回资源对应的操作权限
     * @param id id
     * @return Permission[].class
     */
    @RequestMapping(value = "/{id}/permissions", method = {RequestMethod.GET})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("id") Long id) {
        return permissionService.find(Restrictions.eq("resource.id", id));
    }

}
