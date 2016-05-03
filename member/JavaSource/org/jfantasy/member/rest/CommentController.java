package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.ForbiddenException;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "members-comments", description = "会员评论")
@RestController
@RequestMapping("/members")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查询会员评论", notes = "返回会员的会员评论")
    @RequestMapping(value = "/{memid}/comments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Comment> search(@PathVariable("memid") Long memberId, Pager<Comment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_member.id", memberId.toString()));
        return this.commentService.findPager(pager, filters);
    }

    @ApiOperation(value = "删除会员评论", notes = "删除会员会员评论")
    @RequestMapping(value = "/{memid}/comments/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("memid") Long memberId, @PathVariable("id") Long id) {
        Comment comment = this.commentService.get(id);
        if (comment == null) {
            throw new NotFoundException("[id =" + id + "]对应的会员评论信息不存在");
        }
        if (!memberId.equals(comment.getMember().getId())) {
            throw new ForbiddenException("[memid=" + memberId + "]不能删除该条评论记录信息,因为它不属于该会员");
        }
        this.commentService.deltele(id);
    }

}
