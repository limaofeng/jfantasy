package org.jfantasy.security.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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


@Api(value = "security-users", description = " 用户 ")
@RestController
@RequestMapping("/users")
public class UserController {

    public static UserResourceAssembler assembler = new UserResourceAssembler();
    private UserDetailsResourceAssembler userDetailsResourceAssembler = new UserDetailsResourceAssembler();

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户", notes = "通过该接口, 筛选后台用户")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(@ApiParam(value = "分页对象", name = "pager") Pager<User> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return assembler.toResources(this.userService.findPager(pager, filters));
    }

    @ApiOperation(value = "获取用户", notes = "通过该接口, 获取用户")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.userService.get(id));
    }

    @ApiOperation(value = "获取用户的详细信息", notes = "通过该接口, 获取详细信息")
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.GET)
    public ResultResourceSupport profile(@PathVariable("id") Long id) {
        return userDetailsResourceAssembler.toResource(this.userService.get(id).getDetails());
    }

    @ApiOperation(value = "添加用户", notes = "通过该接口, 添加用户")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport create(@RequestBody User user) {
        return assembler.toResource(this.userService.save(user));
    }

    @ApiOperation(value = "删除用户", notes = "通过该接口, 删除用户")
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }

    @ApiOperation(value = "批量删除用户", notes = "通过该接口, 批量删除用户")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.userService.delete(id);
    }

    @ApiOperation(value = "更新用户", notes = "通过该接口, 更新用户")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public ResultResourceSupport update(@PathVariable("id") Long id, User user) {
        user.setId(id);
        return assembler.toResource(this.userService.save(user));
    }

    @ApiOperation(value = "获取用户授权的菜单信息")
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
