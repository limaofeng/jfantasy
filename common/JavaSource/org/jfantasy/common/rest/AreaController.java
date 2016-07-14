package org.jfantasy.common.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.common.bean.Area;
import org.jfantasy.common.rest.models.assembler.AreaResourceAssembler;
import org.jfantasy.common.service.AreaService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "common-area", description = "地区信息")
@RestController
@RequestMapping("/areas")
public class AreaController {

    private AreaResourceAssembler assembler = new AreaResourceAssembler();

    @Autowired
    private AreaService areaService;

    @JsonResultFilter(value = List.class,ignore = {@IgnoreProperty(pojo = Area.class, name = {"order", "payment", "payConfig"})})
    @ApiOperation(value = "查询地区", notes = "筛选地区，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Area> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.areaService.findPager(pager, filters));
    }

    @ApiOperation(value = "获取地区的子集")
    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> children(@PathVariable("id") String id) {
        return assembler.toResources(this.areaService.get(id).getChildren());
    }

    @ApiOperation(value = "获取地区")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(this.areaService.get(id));
    }

    @ApiOperation(value = "删除地区")
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        this.areaService.delete(id);
    }

    @ApiOperation(value = "批量删除地区")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... id) {
        this.areaService.delete(id);
    }

    @ApiOperation(value = "添加地区")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport create(@RequestBody Area Area) {
        return assembler.toResource(areaService.save(Area));
    }

    @ApiOperation(value = "更新地区")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody Area area) {
        area.setId(id);
        return assembler.toResource(areaService.save(area));
    }

}