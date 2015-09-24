package com.fantasy.member.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "members", description = "会员接口")
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "查询会员信息", notes = "通过 filters 可以过滤数据<br/>本接口支持 <br/> X-Page-Fields、X-Result-Fields、X-Expend-Fields 功能", response = Member[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Member> search(@ApiParam(hidden = true) Pager<Member> pager, @ApiParam(value = "筛选条件", name = "filters") List<PropertyFilter> filters) {
        return this.memberService.findPager(pager, filters);
    }

    @ApiOperation(value = "会员登录", notes = "会员登录接口")
    @RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
    @ResponseBody
    public Member login(@PathVariable("username") String username, @RequestBody String password) {
        return this.memberService.login(username, password);
    }

    @ApiOperation(value = "会员登出", notes = "会员登出接口")
    @RequestMapping(value = "/{username}/register", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable("username") String username) {
        this.memberService.logout(username);
    }

    @ApiOperation(value = "会员注册", notes = "会员注册接口")
    @RequestMapping(value = "/{username}/register", method = RequestMethod.POST)
    @ResponseBody
    public Member register(@PathVariable("username") String username, @RequestBody Member member) {
        member.setUsername(username);
        return this.memberService.register(member);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Member view(@PathVariable("id") Long id) {
        return this.memberService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Member create(@RequestBody @Valid Member member) {
        return memberService.save(member);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Member update(@PathVariable("id") Long id, @RequestBody Member member) {
        member.setId(id);
        return memberService.save(member);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.memberService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.memberService.delete(id);
    }

}
