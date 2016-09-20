package org.jfantasy.member.rest;

import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.Tag;
import org.jfantasy.member.rest.models.TagForm;
import org.jfantasy.member.service.TagService;
import org.jfantasy.member.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 团队标签接口 **/
@RestController
@RequestMapping("/teams/{id}/tags")
public class TeamTagController {

    private static final String TAG_TYPE_TEAM = "team";

    private final TagService tagService;
    private final TeamService teamService;

    @Autowired
    public TeamTagController(TagService tagService, TeamService teamService) {
        this.tagService = tagService;
        this.teamService = teamService;
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 获取团队标签 **/
    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> tags(@PathVariable("id") String id, @RequestParam("type") String type) {
        if (teamService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        return this.tagService.find(TAG_TYPE_TEAM, id, type);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 添加团队标签 **/
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Tag tags(@PathVariable("id") String id, @Validated(RESTful.POST.class) @RequestBody TagForm from) {
        if (teamService.get(id) == null) {
            throw new NotFoundException(id + "不存在!");
        }
        return this.tagService.save(TAG_TYPE_TEAM, id, from.getName(), from.getType());
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Tag.class, name = {"name", "id", "type"}))
    /** 修改团队标签 **/
    @RequestMapping(value = "/{tagid}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseBody
    public Tag tags(@PathVariable("id") String id, @PathVariable("tagid") Long tagid, @Validated(RESTful.PATCH.class) @RequestBody TagForm from) {
        return this.tagService.update(TAG_TYPE_TEAM, id, tagid, from.getName());
    }

    /** 删除团队标签 **/
    @RequestMapping(value = "/{tagid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@PathVariable("id") String id, @PathVariable("tagid") Long tagid) {
        this.tagService.delete(TAG_TYPE_TEAM, id, tagid);
    }

}
