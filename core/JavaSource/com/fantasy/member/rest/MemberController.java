package com.fantasy.member.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @apiDefine paramMember
 * @apiParam {String} username  用户登录名称
 * @apiParam {String} password  登录密码
 * @apiParam {String} nickName  用户显示昵称
 * @apiParam {boolean} enabled  是否启用
 * @apiParam {String} details.name  姓名
 * @apiParam {Enum} details.sex  性别
 * @apiParam {Date} details.birthday  生日
 * @apiParam {String} details.mobile  移动电话
 * @apiParam {String} details.tel  固定电话
 * @apiParam {String} details.email  邮箱
 * @apiParam {String} details.website  网址
 * @apiParam {String} details.description  描述信息
 * @apiParam {Boolean} details.vip  是否为vip用户
 * @apiParam {Integer} details.score  用户积分
 * @apiParam {FileDetail} details.avatar  用户头像存储
 * @apiVersion 3.3.4
 */

/**
 * @apiDefine returnMember
 * @apiSuccess {String} username  用户登录名称
 * @apiSuccess {String} password  登录密码
 * @apiSuccess {String} nickName  用户显示昵称
 * @apiSuccess {boolean} enabled  是否启用
 * @apiSuccess {boolean} accountNonExpired  未过期
 * @apiSuccess {boolean} accountNonLocked  未锁定
 * @apiSuccess {boolean} credentialsNonExpired  未失效
 * @apiSuccess {Date} lockTime  锁定时间
 * @apiSuccess {Date} lastLoginTime  最后登录时间
 * @apiSuccess {String} details.name  姓名
 * @apiSuccess {Enum} details.sex  性别
 * @apiSuccess {Date} details.birthday  生日
 * @apiSuccess {String} details.mobile  移动电话
 * @apiSuccess {String} details.tel  固定电话
 * @apiSuccess {String} details.email  邮箱
 * @apiSuccess {String} details.website  网址
 * @apiSuccess {String} details.description  描述信息
 * @apiSuccess {Boolean} details.vip  是否为vip用户
 * @apiSuccess {Integer} details.score  用户积分
 * @apiSuccess {FileDetail} details.avatar  用户头像存储
 * @apiVersion 3.3.4
 */
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;


    /**
     * @api {get} /members   查询会员
     * @apiVersion 3.3.8
     * @apiName searchMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 筛选会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/members?currentPage=1&LIKES_username=hebo
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.GET)
    public Pager<Member> search(Pager<Member> pager, List<PropertyFilter> filters) {
        return this.memberService.findPager(pager, filters);
    }


    /**
     * @api {get} /members/:id   获取会员
     * @apiVersion 3.3.8
     * @apiName getMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 获取会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/members/43
     * @apiUse returnMember
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Member view(@PathVariable("id") Long id) {
        return this.memberService.get(id);
    }

    /**
     * @api {post} /members  添加会员
     * @apiVersion 3.3.8
     * @apiName createMember
     * @apiGroup 会员管理
     * @apiDescription 添加会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST http://localhost/members
     * @apiUse paramMember
     * @apiUse returnMember
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Member create(Member member) {
        return memberService.save(member);
    }


    /**
     * @api {put} /members/:id  更新会员
     * @apiVersion 3.3.8
     * @apiName updateMember
     * @apiGroup 会员管理
     * @apiDescription 更新会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/members/1
     * @apiUse paramMember
     * @apiUse returnMember
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public Member update(@PathVariable("id") Long id,@RequestBody Member member) {
        member.setId(id);
        return memberService.save(member);
    }


    /**
     * @api {delete} /members/:id   删除会员
     * @apiVersion 3.3.8
     * @apiName deleteMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 删除会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/members/43
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.memberService.delete(id);
    }


    /**
     * @param id 会员ID-1
     * @api {batchDelete} /members   批量删除会员
     * @apiVersion 3.3.8
     * @apiName batchDeleteMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 批量删除会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/members
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.memberService.delete(id);
    }


}
