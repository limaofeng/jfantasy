package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Point;
import org.jfantasy.pay.rest.models.PointForm;
import org.jfantasy.pay.rest.models.assembler.PointResourceAssembler;
import org.jfantasy.pay.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "points", description = "积分")
@RestController
@RequestMapping("/points")
public class PointController {

    private PointResourceAssembler assembler = new PointResourceAssembler();

    @Autowired
    private PointService pointService;

    @JsonResultFilter(allow = @AllowProperty(pojo = Account.class, name = {"sn", "type"}))
    @ApiOperation("积分查询")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Point> pager, List<PropertyFilter> filters) {
        return assembler.toResources(pointService.findPager(pager, filters));
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Account.class, name = {"sn", "type"}))
    @ApiOperation(value = "提交积分记录", notes = "消费与新增积分")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport save(Point point) {
        return assembler.toResource(pointService.save(point));
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = Account.class, name = {"sn", "type"}))
    @ApiOperation(value = "更新积分记录", notes = "该方法只能修改 积分状态 ")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody PointForm form) {
        return assembler.toResource(pointService.update(id, form.getStatus(), form.getRemark()));
    }

}