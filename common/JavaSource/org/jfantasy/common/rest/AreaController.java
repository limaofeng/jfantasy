package org.jfantasy.common.rest;

import org.jfantasy.common.bean.Area;
import org.jfantasy.common.rest.models.assembler.AreaResourceAssembler;
import org.jfantasy.common.service.AreaService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 地区信息 **/
@RestController
@RequestMapping("/areas")
public class AreaController {

    private AreaResourceAssembler assembler = new AreaResourceAssembler();

    @Autowired
    private AreaService areaService;

    @JsonResultFilter(value = List.class)
    /** 查询地区 - 筛选地区，返回通用分页对象 **/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Area> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.areaService.findPager(pager, filters));
    }

    /** 获取地区的子集 **/
    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> children(@PathVariable("id") String id) {
        return assembler.toResources(this.areaService.get(id).getChildren());
    }

    /** 获取地区 **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(this.areaService.get(id));
    }

    /** 删除地区 **/
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        this.areaService.delete(id);
    }

    /** 批量删除地区 **/
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... id) {
        this.areaService.delete(id);
    }

    /** 添加地区 **/
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport create(@RequestBody Area Area) {
        return assembler.toResource(areaService.save(Area));
    }

    /** 更新地区 **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody Area area) {
        area.setId(id);
        return assembler.toResource(areaService.save(area));
    }

}