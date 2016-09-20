package org.jfantasy.member.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.rest.models.CommentShowForm;
import org.jfantasy.member.rest.models.assembler.CommentResourceAssembler;
import org.jfantasy.member.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** 评论 **/
@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentResourceAssembler assembler = new CommentResourceAssembler();

    @Autowired
    private CommentService commentService;

    @JsonResultFilter(allow = @AllowProperty(pojo = Member.class, name = {"id", "nick_name"}))
    /** 查询评论 - 返回会员的会员评论 **/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Comment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("NULL_forComment"));
        return assembler.toResources(this.commentService.findPager(pager, filters));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(HttpServletRequest request, @Validated(RESTful.POST.class) @RequestBody Comment comment) {
        comment.setIp(WebUtil.getRealIpAddress(request));
        return assembler.toResource(this.commentService.save(comment));
    }

    @RequestMapping(value = "/{id}/show", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") Long id, @RequestBody CommentShowForm form) {
        return assembler.toResource(this.commentService.updateShow(id, form.isShow()));
    }

    /** 查看评论 **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    /** 删除评论 - 删除会员会员评论 **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.commentService.deltele(id);
    }

    protected Comment get(Long id) {
        return this.commentService.get(id);
    }
}
