package com.fantasy.common.rest;

import com.fantasy.common.bean.Area;
import com.fantasy.common.service.AreaService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "common-area", description = "地区信息")
@RestController
@RequestMapping("/common/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "查询地区", notes = "筛选地区，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Area> search(Pager<Area> pager, List<PropertyFilter> filters) {
        return this.areaService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取地区")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Area view(@PathVariable("id") String id) {
        return this.areaService.get(id);
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
    public Area create(@RequestBody Area Area) {
        return areaService.save(Area);
    }

    @ApiOperation(value = "更新地区")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Area update(@PathVariable("id") String id, @RequestBody Area area) {
        area.setId(id);
        return areaService.save(area);
    }

}
