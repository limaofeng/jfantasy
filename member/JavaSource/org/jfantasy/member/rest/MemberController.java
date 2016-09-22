package org.jfantasy.member.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.member.bean.*;
import org.jfantasy.member.bean.enums.TeamMemberStatus;
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

/**
 * 会员接口
 **/
@RestController
@RequestMapping("/members")
public class MemberController {

    public static MemberResourceAssembler assembler = new MemberResourceAssembler();
    private static ProfileResourceAssembler profileAssembler = new ProfileResourceAssembler();

    private final MemberService memberService;
    private final FavoriteService favoriteService;
    @Autowired
    private TeamController teamController;
    @Autowired
    private InvoiceController invoiceController;
    @Autowired
    private CommentController commentController;
    @Autowired
    private ReceiverController receiverController;

    @Autowired
    public MemberController(MemberService memberService, FavoriteService favoriteService) {
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Member.class, name = {"password", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"}),
            allow = @AllowProperty(pojo = MemberDetails.class, name = {"name", "sex", "birthday", "avatar"})
    )
    /**
     * 查询会员信息<br/>
     * 通过 filters 可以过滤数据<br/>本接口支持 <br/> X-Page-Fields、X-Result-Fields、X-Expend-Fields 功能
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Member> pager, List<PropertyFilter> filters) {
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

    /**
     * 获取用户的详细信息
     *
     * @param response HttpServletResponse
     * @param id       id
     * @return ResultResourceSupport
     */
    @JsonResultFilter(
            allow = @AllowProperty(pojo = Member.class, name = {"id", "target_id", "target_type", "type", "username"})
    )
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

    @JsonResultFilter(
            allow = @AllowProperty(pojo = Member.class, name = {"id", "target_id", "target_type", "type", "username"})
    )
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport profile(HttpServletResponse response, @PathVariable("id") Long id,@RequestBody MemberDetails details) {
        Member member = get(id);
        if (Member.MEMBER_TYPE_PERSONAL.equals(member.getType())) {
            details.setMemberId(id);
            return profileAssembler.toResource(memberService.update(details));
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
            ignore = @IgnoreProperty(pojo = Member.class, name = {"password", "enabled", "account_nonExpired", "accountNonLocked", "credentialsNonExpired"}),
            allow = @AllowProperty(pojo = MemberDetails.class, name = {"name", "sex", "birthday", "avatar"})
    )
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport password(@PathVariable("id") Long id, @RequestBody PasswordForm form) {
        return assembler.toResource(this.memberService.changePassword(id, form.getType(), form.getOldPassword(), form.getNewPassword()));
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

    /**
     * 查询会员评论 - 返回会员的会员评论
     *
     * @param memberId
     * @param pager
     * @param filters
     * @return
     */
    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Comment.class, name = {"member"})
    )
    @RequestMapping(value = "/{memid}/comments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> comments(@PathVariable("memid") Long memberId, Pager<Comment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.commentController.search(pager, filters);
    }

    /**
     * 查询会员收货地址 - 返回会员的会员评论
     *
     * @param memberId
     * @param filters
     * @return
     */
    @RequestMapping(value = "/{memid}/receivers", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> receivers(@PathVariable("memid") Long memberId, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.receiverController.search(filters);
    }

    /**
     * 查询会员的开票信息
     *
     * @param memberId
     * @param pager
     * @param filters
     * @return
     */
    @JsonResultFilter(allow = @AllowProperty(pojo = Member.class, name = {"id", "nick_name"}))
    @RequestMapping(value = "/{memid}/invoices", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> invoices(@PathVariable("memid") Long memberId, Pager<Invoice> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.invoiceController.search(pager, filters);
    }


    /**
     * 查询会员的团队信息
     *
     * @param memberId
     * @param type
     * @param filters
     * @return
     */
    @RequestMapping(value = "/{memid}/teams", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> teams(@PathVariable("memid") Long memberId, @RequestParam(value = "type", required = false) String type, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_teamMembers.member.id", memberId.toString()));//包含当前会员
        filters.add(new PropertyFilter("EQE_teamMembers.status", TeamMemberStatus.activated));//状态有效
        return teamController.search(type, new Pager<Team>(1000), filters).getPageItems();
    }

}
