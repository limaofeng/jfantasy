package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.rest.PayConfigController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


public class PayConfigResourceAssembler extends ResourceAssemblerSupport<PayConfig, ResultResourceSupport> {

    public PayConfigResourceAssembler() {
        super(PayConfigController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport instantiateResource(PayConfig entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(PayConfig entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(),entity);
        resource.add(linkTo(methodOn(PayConfigController.class).payments(entity.getId().toString(),new Pager<Payment>(),new ArrayList<PropertyFilter>())).withRel("payments"));
        resource.add(linkTo(methodOn(PayConfigController.class).refunds(entity.getId().toString(),new Pager<Refund>(),new ArrayList<PropertyFilter>())).withRel("refunds"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<PayConfig> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
