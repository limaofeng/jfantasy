package com.fantasy.member.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
 * @apiParam {String} username  用户登录名称
 * @apiParam {String} password  登录密码
 * @apiParam {String} nickName  用户显示昵称
 * @apiParam {boolean} enabled  是否启用
 * @apiParam {boolean} accountNonExpired  未过期
 * @apiParam {boolean} accountNonLocked  未锁定
 * @apiParam {boolean} credentialsNonExpired  未失效
 * @apiParam {Date} lockTime  锁定时间
 * @apiParam {Date} lastLoginTime  最后登录时间
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
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;


    /**
     * @api {get} /members   查询用户
     * @apiVersion 3.3.7
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
     * @apiVersion 3.3.7
     * @apiName getMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 获取会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/members/43
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Member view(@PathVariable("id") Long id) {
        return this.memberService.get(id);
    }

    /**
     * @param member member
     * @return member
     * @api {post} /members  添加会员
     * @apiVersion 3.3.7
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
    public Member create(Member member) {
        return memberService.save(member);
    }


    /**
     * @param member member
     * @param id memberId
     * @return member
     * @api {put} /members/:id  更新会员
     * @apiVersion 3.3.7
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
    @RequestMapping(value="/{id}",method = {RequestMethod.PUT})
    public Member update(@PathVariable("id") Long id,Member member) {
        member.setId(id);
        return memberService.save(member);
    }



    /**
     * @api {delete} /members/:id   删除会员
     * @apiVersion 3.3.7
     * @apiName deleteMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 删除会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/members/43
     * @apiUse paramArticle
     * @apiUse GeneralError
     * @param id memberID
     */
    @RequestMapping(value="/{id}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable("id") Long id) {
        this.memberService.delete(id);
    }


    /**
     * @api {batchDelete} /members   批量删除会员
     * @apiVersion 3.3.7
     * @apiName batchDeleteMember
     * @apiGroup 会员管理
     * @apiDescription 通过该接口, 批量删除会员
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/members
     * @apiUse GeneralError
     * @param id  会员ID-1
     * @param id  会员ID-2
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(Long... id) {
        this.memberService.delete(id);
    }



}
