package org.jfantasy.security.rest;


import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.rest.models.assembler.UserDetailsResourceAssembler;
import org.jfantasy.security.rest.models.assembler.UserResourceAssembler;
import org.jfantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    public static UserResourceAssembler assembler = new UserResourceAssembler();
    private UserDetailsResourceAssembler userDetailsResourceAssembler = new UserDetailsResourceAssembler();

    @Autowired
    private UserService userService;

    /**
     * 查询用户
     * @param pager
     * @param filters
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<User> pager,List<PropertyFilter> filters) {
        return assembler.toResources(this.userService.findPager(pager, filters));
    }

    /**
     * 获取用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.userService.get(id));
    }

    /**
     * 获取用户的详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.GET)
    public ResultResourceSupport profile(@PathVariable("id") Long id) {
        return userDetailsResourceAssembler.toResource(this.userService.get(id).getDetails());
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport create(@RequestBody User user) {
        return assembler.toResource(this.userService.save(user));
    }

    /**
     * 删除用户
     * @param id
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }

    /**
     * 批量删除用户
     * @param id
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.userService.delete(id);
    }

    /**
     * 更新用户
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    public ResultResourceSupport update(@PathVariable("id") Long id, User user) {
        user.setId(id);
        return assembler.toResource(this.userService.save(user));
    }

    /**
     * 获取用户授权的菜单信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/menus", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> menus(@PathVariable("id") Long id) {
        Set<String> menuIds = new HashSet<>();
        for (Role role : this.get(id).getRoles()) {
            menuIds.addAll(Arrays.asList(ObjectUtil.toFieldArray(role.getMenus(), "id", String.class)));
        }
        return new ArrayList<>(menuIds);
    }

    private User get(Long id) {
        return this.userService.get(id);
    }

}
