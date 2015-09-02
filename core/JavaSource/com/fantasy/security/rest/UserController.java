package com.fantasy.security.rest;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @apiDefine paramUser
 * @apiParam {String} username  登录名称
 * @apiParam {String} password  登录密码
 * @apiParam {String} nickName  显示昵称
 * @apiParam {boolean} enabled  是否启用
 * @apiParam {Website} website.id  网站对象
 * @apiParam {List} roles  用户对应的角色
 * @apiParam {String} details.name  姓名
 * @apiParam {Sex} details.sex  性别
 * @apiParam {Date} details.birthday  生日
 * @apiParam {String} details.mobile  移动电话
 * @apiParam {String} details.tel  固定电话
 * @apiParam {String} details.email  邮箱
 * @apiParam {String} details.website  网址
 * @apiParam {String} details.description  描述信息
 * @apiParam {FileDetail} details.Avatar  用户头像图片对象
 * @apiVersion 3.3.8
 */

/**
 * @apiDefine returnUser
 * @apiSuccess {Long} id  用户ID
 * @apiSuccess {String} username  登录名称
 * @apiSuccess {String} password  登录密码
 * @apiSuccess {String} nickName  显示昵称
 * @apiSuccess {boolean} enabled  是否启用
 * @apiSuccess {boolean} accountNonExpired  未过期
 * @apiSuccess {boolean} accountNonLocked  未锁定
 * @apiSuccess {boolean} credentialsNonExpired  未失效
 * @apiSuccess {Date} lockTime  锁定时间
 * @apiSuccess {Date} lastLoginTime 最后登录时间
 * @apiSuccess {List} roles  用户对应的角色
 * @apiSuccess {Website} website  网站对象
 * @apiSuccess {String} details.name  姓名
 * @apiSuccess {Sex} details.sex  性别
 * @apiSuccess {Date} details.birthday  生日
 * @apiSuccess {String} details.mobile  移动电话
 * @apiSuccess {String} details.tel  固定电话
 * @apiSuccess {String} details.email  邮箱
 * @apiSuccess {String} details.website  网址
 * @apiSuccess {String} details.description  描述信息
 * @apiSuccess {FileDetail} details.Avatar  用户头像图片对象
 * @apiVersion 3.3.8
 */
@Api(value = "security-users", description = " 用户管理 ")
@RestController
@RequestMapping("/security/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @api {get} /users   查询用户
     * @apiVersion 3.3.8
     * @apiName searchUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 筛选后台用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/users?currentPage=1&LIKES_username=hebo
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse GeneralError
     */
    @ApiOperation(value = "查询用户", notes = "通过该接口, 筛选后台用户")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<User> search(@ApiParam(value = "分页对象", name = "pager") Pager<User> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.userService.findPager(pager, filters);
    }


    /**
     * @api {get} /users/:username   获取用户
     * @apiVersion 3.3.8
     * @apiName getUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 获取用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/users/hebo
     * @apiUse returnUser
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User view(@PathVariable("username") String username) {
        return this.userService.findUniqueByUsername(username);
    }

    /**
     * @api {post} /users   添加用户
     * @apiVersion 3.3.8
     * @apiName createUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 添加用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "username=hello&password=123456&..." http://localhost/users
     * @apiUse paramUser
     * @apiUse returnUser
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    public User create(User user) {
        return this.userService.save(user);
    }

    /**
     * @api {delete} /users/:id   删除用户
     * @apiVersion 3.3.8
     * @apiName deleteUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 删除用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/users/43
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }


    /**
     * @param id 用户ID-1
     * @param id 用户ID-2
     * @api {batchDelete} /users   批量删除用户
     * @apiVersion 3.3.8
     * @apiName batchDeleteUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 批量删除用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/users
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(Long... id) {
        this.userService.delete(id);
    }

    /**
     * @api {put} /users/:id   更新用户
     * @apiVersion 3.3.8
     * @apiName updateUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 更新用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/users/43
     * @apiUse paramUser
     * @apiUse returnUser
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public User update(@PathVariable("id") Long id, User user) {
        user.setId(id);
        return this.userService.save(user);
    }
}
