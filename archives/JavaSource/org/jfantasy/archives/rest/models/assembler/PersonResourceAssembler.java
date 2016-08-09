package org.jfantasy.archives.rest.models.assembler;

import org.jfantasy.archives.bean.Person;
import org.jfantasy.archives.bean.Record;
import org.jfantasy.archives.rest.PersonController;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, ResultResourceSupport> {

    public PersonResourceAssembler() {
        super(PersonController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Person> instantiateResource(Person entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Person entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(PersonController.class).records(entity.getId(), new Pager<Record>(), new ArrayList<PropertyFilter>())).withRel("records"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Person> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
