package org.jfantasy.common.rest.models.assembler;

import org.jfantasy.common.bean.Area;
import org.jfantasy.common.rest.AreaController;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class AreaResourceAssembler extends ResourceAssemblerSupport<Area, ResultResourceSupport> {

    public AreaResourceAssembler() {
        super(AreaController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Area> instantiateResource(Area entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Area entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(AreaController.class).children(entity.getId())).withRel("children"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Area> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
