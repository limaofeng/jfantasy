package org.jfantasy.security.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Permission;
import org.jfantasy.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private transient PermissionService permissionService;

    /**
     * 查询权限<br/>
     * 筛选权限，返回通用分页对象
     * @param pager 分页对象
     * @param filters 过滤条件
     * @return Pager<Permission>
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Permission> search(Pager<Permission> pager, List<PropertyFilter> filters) {
        return this.permissionService.findPager(pager, filters);
    }

    /**
     * 删除权限<br/>
     * 通过权限ID,删除权限
     * @param id id
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.permissionService.delete(id);
    }

    /**
     * 批量删除权限
     * @param id
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.permissionService.delete(id);
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Permission create(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    /**
     * 更新权限
     * @param id
     * @param permission
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    @ResponseBody
    public Permission update(@PathVariable("id") Long id, @RequestBody Permission permission) {
        permission.setId(id);
        return permissionService.save(permission);
    }

}
