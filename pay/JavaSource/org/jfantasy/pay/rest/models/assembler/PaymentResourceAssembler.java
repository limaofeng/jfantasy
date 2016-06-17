package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.rest.PaymentController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PaymentResourceAssembler extends ResourceAssemblerSupport<Payment, ResultResourceSupport> {

    public PaymentResourceAssembler() {
        super(PaymentController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Payment> instantiateResource(Payment entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Payment entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getSn(),entity);
        resource.add(linkTo(methodOn(PaymentController.class).order(entity.getSn())).withRel("order"));
        resource.add(linkTo(methodOn(PaymentController.class).payconfig(entity.getSn())).withRel("payconfig"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Payment> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
