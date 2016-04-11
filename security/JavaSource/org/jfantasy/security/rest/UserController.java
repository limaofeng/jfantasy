package org.jfantasy.security.rest;


import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.bean.UserDetails;
import org.jfantasy.security.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "security-users", description = " 用户 ")
@RestController
@RequestMapping("/security/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户", notes = "通过该接口, 筛选后台用户")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<User> search(@ApiParam(value = "分页对象", name = "pager") Pager<User> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.userService.findPager(pager, filters);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
    @ResponseBody
    public User login(@PathVariable("username") String username, @RequestBody String password) {
        return this.userService.login(username, password);
    }

    @ApiOperation(value = "用户登出", notes = "用户登出接口")
    @RequestMapping(value = "/{username}/logout", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable("username") String username) {
        this.userService.logout(username);
    }

    @ApiOperation(value = "获取用户", notes = "通过该接口, 获取用户")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User view(@PathVariable("id") Long id) {
        return this.userService.get(id);
    }

    @ApiOperation(value = "获取用户的详细信息", notes = "通过该接口, 获取详细信息")
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.GET)
    public UserDetails profile(@PathVariable("id") Long id) {
        return this.userService.get(id).getDetails();
    }

    @ApiOperation(value = "添加用户", notes = "通过该接口, 添加用户")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public User create(@RequestBody User user) {
        return this.userService.save(user);
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
    public User update(@PathVariable("id") Long id, User user) {
        user.setId(id);
        return this.userService.save(user);
    }

}
