package org.jfantasy.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.security.bean.Menu;
import org.jfantasy.security.bean.Permission;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.service.PermissionService;
import org.jfantasy.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "security-roles", description = "角色")
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private transient RoleService roleService;
    @Autowired
    private transient PermissionService permissionService;

    @ApiOperation(value = "按条件角色", notes = "筛选角色，返回通用分页对象", response = Pager.class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Role> search(@ApiParam(value = "分页对象", name = "pager") Pager<Role> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.roleService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{code}", method = {RequestMethod.GET})
    @ResponseBody
    public Role view(@PathVariable("code") String code) {
        return roleService.get(code);
    }

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Role create(@RequestBody Role role) {
        return roleService.save(role);
    }

    @RequestMapping(value = "/{code}", method = {RequestMethod.PATCH})
    @ResponseBody
    public Role update(@PathVariable("code") String code, @RequestBody Role role) {
        role.setCode(code);
        return roleService.save(role);
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("code") String code) {
        this.roleService.delete(code);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... codes) {
        this.roleService.delete(codes);
    }

    @ApiOperation(value = "返回角色的授权菜单", response = String[].class)
    @RequestMapping(value = "/{code}/menus", method = {RequestMethod.GET})
    @ResponseBody
    public String[] menus(@PathVariable("code") String code) {
        return ObjectUtil.toFieldArray(get(code).getMenus(), "id", String.class);
    }

    @ApiOperation(value = "更新角色菜单权限", notes = "返回角色对应的菜单授权", response = String[].class)
    @RequestMapping(value = "/{code}/menus", method = {RequestMethod.PATCH})
    @ResponseBody
    public String[] menus(@PathVariable("code") String code, @RequestBody String[] menuIds) {
        Role role = this.roleService.get(code);
        role.getMenus().clear();
        for (String menuId : menuIds) {
            role.getMenus().add(new Menu(menuId));
        }
        return ObjectUtil.toFieldArray(this.roleService.save(role).getMenus(), "id", String.class);
    }

    @ApiOperation(value = "返回角色权限", notes = "返回角色对应的操作权限", response = Permission[].class)
    @RequestMapping(value = "/{code}/permissions", method = {RequestMethod.GET})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("code") String code) {
        return permissionService.find(Restrictions.eq("roles.code", code));
    }

    @ApiOperation(value = "为角色添加权限", notes = "为角色添加操作权限,返回权限详细信息")
    @RequestMapping(value = "/{code}/permissions", method = {RequestMethod.POST})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("code") String code, @RequestBody Long... permissionId) {
        throw new RuntimeException("该方法未实现!");
    }

    private Role get(String code) {
        return this.roleService.get(code);
    }

}