package com.fantasy.security.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security/resources")
public class ResourceController {

    @Autowired
    private transient ResourceService resourceService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Resource> search(Pager<Resource> pager, List<PropertyFilter> filters) {
        return this.resourceService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.resourceService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.resourceService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Resource create(@RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Resource update(@PathVariable("id") Long id, @RequestBody Resource resource) {
        resource.setId(id);
        return resourceService.save(resource);
    }

}
