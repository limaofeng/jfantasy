package org.jfantasy.security.rest;

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

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private transient RoleService roleService;
    @Autowired
    private transient PermissionService permissionService;

    /**
     * 按条件角色
     * @param pager
     * @param filters
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Role> search(Pager<Role> pager,List<PropertyFilter> filters) {
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

    /**
     * 返回角色的授权菜单
     * @param code
     * @return
     */
    @RequestMapping(value = "/{code}/menus", method = {RequestMethod.GET})
    @ResponseBody
    public String[] menus(@PathVariable("code") String code) {
        return ObjectUtil.toFieldArray(get(code).getMenus(), "id", String.class);
    }

    /**
     * 更新角色菜单权限
     * @param code
     * @param menuIds
     * @return
     */
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

    /**
     * 返回角色权限
     * @param code
     * @return
     */
    @RequestMapping(value = "/{code}/permissions", method = {RequestMethod.GET})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("code") String code) {
        return permissionService.find(Restrictions.eq("roles.code", code));
    }

    /**
     * 为角色添加权限
     * @param code
     * @param permissionId
     * @return
     */
    @RequestMapping(value = "/{code}/permissions", method = {RequestMethod.POST})
    @ResponseBody
    public List<Permission> permissions(@PathVariable("code") String code, @RequestBody Long... permissionId) {
        throw new RuntimeException("该方法未实现!");
    }

    private Role get(String code) {
        return this.roleService.get(code);
    }

}