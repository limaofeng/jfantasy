package com.fantasy.security.rest;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @apiDefine paramUser
 * @apiParam {String} username  登录名称
 * @apiParam {String} password  登录密码
 * @apiParam {String} nickName  显示昵称
 * @apiParam {boolean} enabled  是否启用
 * @apiParam {Website} website.id  网站对象
 * @apiParam {List<Role>} roles  用户对应的角色
 * @apiParam {String} details.name  姓名
 * @apiParam {Sex} details.sex  性别
 * @apiParam {Date} details.birthday  生日
 * @apiParam {String} details.mobile  移动电话
 * @apiParam {String} details.tel  固定电话
 * @apiParam {String} details.email  邮箱
 * @apiParam {String} details.website  网址
 * @apiParam {String} details.description  描述信息
 * @apiParam {FileDetail} details.Avatar  用户头像图片对象
 * @apiVersion 3.3.6
 */

/**
 * @apiDefine returnUser
 * @apiParam {Long} id  用户ID
 * @apiParam {String} username  登录名称
 * @apiParam {String} password  登录密码
 * @apiParam {String} nickName  显示昵称
 * @apiParam {boolean} enabled  是否启用
 * @apiParam {boolean} accountNonExpired  未过期
 * @apiParam {boolean} accountNonLocked  未锁定
 * @apiParam {boolean} credentialsNonExpired  未失效
 * @apiParam {Date} lockTime  锁定时间
 * @apiParam {Date} lastLoginTime 最后登录时间
 * @apiParam {List<Role>} roles  用户对应的角色
 * @apiParam {Website} website  网站对象
 * @apiParam {String} details.name  姓名
 * @apiParam {Sex} details.sex  性别
 * @apiParam {Date} details.birthday  生日
 * @apiParam {String} details.mobile  移动电话
 * @apiParam {String} details.tel  固定电话
 * @apiParam {String} details.email  邮箱
 * @apiParam {String} details.website  网址
 * @apiParam {String} details.description  描述信息
 * @apiParam {FileDetail} details.Avatar  用户头像图片对象
 * @apiVersion 3.3.6
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @api {get} /users   分页条件查询用户
     * @apiVersion 3.3.6
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
    @RequestMapping(method = RequestMethod.GET)
    public Pager<User> search(Pager<User> pager, List<PropertyFilter> filters) {
        return this.userService.findPager(pager, filters);
    }


    /**
     * @api {get} /users/:username   获取用户
     * @apiVersion 3.3.6
     * @apiName getUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 获取用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/users/hebo
     * @apiUse returnArticle
     * @apiUse GeneralError
     * @param username 用户名
     * @return User
     */
    @RequestMapping(value="/{username}",method = RequestMethod.GET)
    public User view(@PathVariable("username") String username) {
        return this.userService.findUniqueByUsername(username);
    }

    /**
     * @api {post} /users   添加用户
     * @apiVersion 3.3.6
     * @apiName createUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 添加用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "username=hello&password=123456&..." http://localhost/users
     * @apiUse paramUser
     * @apiUse returnUser
     * @apiUse GeneralError
     * @param user 用户对象paramUser
     */
    @RequestMapping(method = {RequestMethod.POST})
    public User create(User user){
        return this.userService.save(user);
    }

    /**
     * @api {delete} /users/:id   删除用户
     * @apiVersion 3.3.6
     * @apiName deleteUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 删除用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/users/43
     * @apiUse GeneralError
     * @param id  用户ID
     */
    @RequestMapping(value="/{id}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }


    /**
     * @api {batchDelete} /users   批量删除用户
     * @apiVersion 3.3.6
     * @apiName batchDeleteUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 批量删除用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/users
     * @apiUse GeneralError
     * @param id  用户ID-1
     * @param id  用户ID-2
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(Long... id) {
        this.userService.delete(id);
    }

    /**
     * @api {put} /users/:id   更新用户
     * @apiVersion 3.3.6
     * @apiName updateUser
     * @apiGroup 用户管理
     * @apiDescription 通过该接口, 更新用户
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/users/43
     * @apiUse paramUser
     * @apiUse returnUser
     * @apiUse GeneralError
     * @param id 用户ID
     * @param user user对象要修改的值
     */
    @RequestMapping(value="/{id}",method = {RequestMethod.PUT})
    public User update(@PathVariable("id") Long id,User user) {
        user.setId(id);
        return this.userService.save(user);
    }
}
