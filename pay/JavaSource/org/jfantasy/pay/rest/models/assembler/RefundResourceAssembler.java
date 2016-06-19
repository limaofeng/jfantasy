package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.rest.RefundController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class RefundResourceAssembler extends ResourceAssemblerSupport<Refund, ResultResourceSupport> {

    public RefundResourceAssembler() {
        super(RefundController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Refund> instantiateResource(Refund entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Refund entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getSn(),entity);
        resource.add(linkTo(methodOn(RefundController.class).order(entity.getSn())).withRel("order"));
        resource.add(linkTo(methodOn(RefundController.class).payconfig(entity.getSn())).withRel("payconfig"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Refund> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
