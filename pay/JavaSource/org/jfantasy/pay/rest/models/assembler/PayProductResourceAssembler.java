package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.rest.PayProductController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


public class PayProductResourceAssembler extends ResourceAssemblerSupport<PayProduct, ResultResourceSupport> {

    public PayProductResourceAssembler() {
        super(PayProductController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<PayProduct> instantiateResource(PayProduct entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(PayProduct entity) {
        ResultResourceSupport resource = instantiateResource(entity);
        resource.add(linkTo(methodOn(PayProductController.class).view(entity.getId())).withSelfRel());
        resource.add(linkTo(methodOn(PayProductController.class).payconfigs(entity.getId(),new Pager<PayConfig>(),new ArrayList<PropertyFilter>())).withRel("payconfigs"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<PayProduct> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
