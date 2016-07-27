package org.jfantasy.archives.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.archives.bean.Person;
import org.jfantasy.archives.bean.Record;
import org.jfantasy.archives.rest.models.assembler.PersonResourceAssembler;
import org.jfantasy.archives.service.PersonService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "persons", description = "档案人员信息")
@RestController
@RequestMapping("/persons")
public class PersonController {

    private PersonResourceAssembler assembler = new PersonResourceAssembler();

    @Autowired
    private PersonService personService;
    @Autowired
    private RecordController recordController;

    @ApiOperation(value = "人员信息列表", notes = "人员信息列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Person> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.personService.findPager(pager, filters));
    }

    @ApiOperation(value = "查看人员信息", notes = "查看人员信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "添加人员信息", notes = "添加人员信息")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResourceSupport create(@RequestBody Person person) {
        return assembler.toResource(this.personService.save(person));
    }

    @ApiOperation(value = "更新人员信息", notes = "更新人员信息地址")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResultResourceSupport update(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Person person) {
        person.setId(id);
        return assembler.toResource(this.personService.update(person, WebUtil.has(request, RequestMethod.PATCH)));
    }

    @ApiOperation(value = "删除人员信息", notes = "删除人员信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.personService.deltele(id);
    }

    @ApiOperation(value = "查看人员的档案信息")
    @RequestMapping(value = "/{id}/records", method = RequestMethod.GET)
    public Pager<ResultResourceSupport> records(@PathVariable("id") Long id, Pager<Record> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_person.id", id.toString()));
        return recordController.search(pager, filters);
    }

    private Person get(Long id) {
        return this.personService.get(id);
    }

}
