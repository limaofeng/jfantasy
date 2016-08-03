package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.Tag;
import org.jfantasy.member.rest.models.TagForm;
import org.jfantasy.member.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "member-tags", description = "会员标签接口")
@RestController
@RequestMapping("/members/{id}/tags")
public class MemberTagController {

    private static final String TAG_TYPE_MEMBER = "member";

    @Autowired
    private TagService tagService;

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    @ApiOperation(value = "获取会员标签")
    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> tags(@PathVariable("id") String id, @RequestParam("type") String type) {
        return this.tagService.find(TAG_TYPE_MEMBER, id, type);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    @ApiOperation(value = "添加会员标签")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Tag tags(@PathVariable("id") String id, @Validated(RESTful.POST.class) @RequestBody TagForm from) {
        return this.tagService.save(TAG_TYPE_MEMBER, id, from.getName(), from.getType());
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    @ApiOperation(value = "修改会员标签")
    @RequestMapping(value = "/{tagid}", method = RequestMethod.PATCH)
    @ResponseBody
    public Tag tags(@PathVariable("id") String id, @PathVariable("tagid") Long tagid, @Validated(RESTful.PATCH.class) @RequestBody TagForm from) {
        return this.tagService.update(TAG_TYPE_MEMBER, id, tagid, from.getName());
    }

    @ApiOperation(value = "删除会员标签")
    @RequestMapping(value = "/{tagid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@PathVariable("id") String id, @PathVariable("tagid") Long tagid) {
        this.tagService.delete(TAG_TYPE_MEMBER, id, tagid);
    }

}
