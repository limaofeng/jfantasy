package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful.POST;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.MemberDetails;
import org.jfantasy.member.rest.models.assembler.MemberResourceAssembler;
import org.jfantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "members", description = "会员接口")
@RestController
@RequestMapping("/members")
public class MemberController {

    public static MemberResourceAssembler assembler = new MemberResourceAssembler();

    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentController commentController;
    @Autowired
    private ReceiverController receiverController;

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Member.class, name = {"password", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"}),
            allow = @AllowProperty(pojo = MemberDetails.class, name = {"name", "sex", "birthday", "avatar"})
    )
    @ApiOperation(value = "查询会员信息", notes = "通过 filters 可以过滤数据<br/>本接口支持 <br/> X-Page-Fields、X-Result-Fields、X-Expend-Fields 功能", response = Member[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(@ApiParam(hidden = true) Pager<Member> pager, @ApiParam(value = "筛选条件", name = "filters") List<PropertyFilter> filters) {
        return assembler.toResources(this.memberService.findPager(pager, filters));
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Member.class, name = {"password"})
    )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Member view(@PathVariable("id") Long id) {
        return this.memberService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Member create(@RequestBody @Validated(POST.class) Member member) {
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

    @ApiOperation(value = "查询会员评论", notes = "返回会员的会员评论")
    @RequestMapping(value = "/{memid}/comments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> comments(@PathVariable("memid") Long memberId, Pager<Comment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.commentController.search(pager, filters);
    }

    @ApiOperation(value = "查询会员收货地址", notes = "返回会员的会员评论")
    @RequestMapping(value = "/{memid}/receivers", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> receivers(@PathVariable("memid") Long memberId, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.receiverController.search(filters);
    }

}
