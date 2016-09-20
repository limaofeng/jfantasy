package org.jfantasy.member.rest;

import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.Tag;
import org.jfantasy.member.rest.models.TagForm;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.member.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/** 会员标签接口 **/
@RestController
@RequestMapping("/members/{id}/tags")
public class MemberTagController {

    private static final String TAG_TYPE_MEMBER = "member";

    @Autowired
    private TagService tagService;
    @Autowired
    private MemberService memberService;

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 获取会员标签 **/
    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> tags(@PathVariable("id") Long id, @RequestParam("type") String type) {
        if (memberService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        return this.tagService.find(TAG_TYPE_MEMBER, id.toString(), type);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 添加会员标签 **/
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Tag tags(@PathVariable("id") Long id, @Validated(RESTful.POST.class) @RequestBody TagForm from) {
        if (memberService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        return this.tagService.save(TAG_TYPE_MEMBER, id.toString(), from.getName(), from.getType());
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 修改会员标签 **/
    @RequestMapping(value = "/{tagid}", method = RequestMethod.PATCH)
    @ResponseBody
    public Tag tags(@PathVariable("id") Long id, @PathVariable("tagid") Long tagid, @Validated(RESTful.PATCH.class) @RequestBody TagForm from) {
        if (memberService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        return this.tagService.update(TAG_TYPE_MEMBER, id.toString(), tagid, from.getName());
    }

    /** 删除会员标签 **/
    @RequestMapping(value = "/{tagid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@PathVariable("id") Long id, @PathVariable("tagid") Long tagid) {
        if (memberService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        this.tagService.delete(TAG_TYPE_MEMBER, id.toString(), tagid);
    }

}
