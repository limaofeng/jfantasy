package org.jfantasy.archives.rest.models.assembler;

import org.jfantasy.archives.bean.Person;
import org.jfantasy.archives.rest.PersonController;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

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
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Person> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
