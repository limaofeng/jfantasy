package org.jfantasy.archives.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.archives.bean.Person;
import org.jfantasy.archives.bean.Record;
import org.jfantasy.archives.rest.models.assembler.RecordResourceAssembler;
import org.jfantasy.archives.service.RecordService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "records", description = "档案接口")
@RestController
@RequestMapping("/records")
public class RecordController {

    private RecordResourceAssembler assembler = new RecordResourceAssembler();

    @Autowired
    private RecordService recordService;

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Person.class, name = {"person"}))
    @ApiOperation(value = "获取档案列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Record> pager, List<PropertyFilter> filters) {
        return assembler.toResources(recordService.findPager(pager, filters));
    }

    @ApiOperation(value = "添加档案")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@Validated(RESTful.POST.class) @RequestBody Record record) {
        return assembler.toResource(this.recordService.save(record));
    }

    @ApiOperation(value = "获取档案详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    private Record get(Long id) {
        return this.recordService.get(id);
    }

}
