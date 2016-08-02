package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Favorite;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.MemberDetails;
import org.jfantasy.member.rest.models.PasswordForm;
import org.jfantasy.member.rest.models.assembler.MemberResourceAssembler;
import org.jfantasy.member.rest.models.assembler.ProfileResourceAssembler;
import org.jfantasy.member.service.FavoriteService;
import org.jfantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "members", description = "会员接口")
@RestController
@RequestMapping("/members")
public class MemberController {

    public static MemberResourceAssembler assembler = new MemberResourceAssembler();
    public static ProfileResourceAssembler profileAssembler = new ProfileResourceAssembler();

    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentController commentController;
    @Autowired
    private ReceiverController receiverController;
    @Autowired
    private FavoriteService favoriteService;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(get(id));
    }

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Favorite.class, name = {"member", "member_id"}))
    @RequestMapping(value = "/{id}/favorites", method = RequestMethod.GET)
    @ResponseBody
    public List<Favorite> favorites(@PathVariable("id") Long id, @RequestParam(value = "type") String type) {
        return this.favoriteService.findByMemberId(id, type);
    }

    @JsonResultFilter(
            allow = @AllowProperty(pojo = Member.class, name = {"id", "target_id", "target_type", "type", "username"})
    )
    @ApiOperation(value = "获取用户的详细信息", notes = "通过该接口, 获取详细信息")
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport profile(HttpServletResponse response, @PathVariable("id") Long id) {
        Member member = get(id);
        if (Member.MEMBER_TYPE_PERSONAL.equals(member.getType())) {
            return profileAssembler.toResource(member.getDetails());
        }
        response.setStatus(307);
        return assembler.toResource(member);
    }

    private Member get(Long id) {
        Member member = this.memberService.get(id);
        if (member == null) {
            throw new NotFoundException("用户不存在");
        }
        return member;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport create(@Validated(RESTful.POST.class) @RequestBody Member member) {
        return assembler.toResource(memberService.save(member));
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Member.class, name = {"password", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"}),
            allow = @AllowProperty(pojo = MemberDetails.class, name = {"name", "sex", "birthday", "avatar"})
    )
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport password(@PathVariable("id") Long id, @RequestBody PasswordForm form) {
        return assembler.toResource(this.memberService.changePassword(id, form.getOldPassword(), form.getNewPassword()));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @ResponseBody
    public ResultResourceSupport update(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Member member) {
        member.setId(id);
        return assembler.toResource(memberService.update(member, WebUtil.has(request, RequestMethod.PATCH)));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
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
