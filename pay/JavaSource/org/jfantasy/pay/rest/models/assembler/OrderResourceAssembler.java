package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.rest.OrderController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OrderResourceAssembler extends ResourceAssemblerSupport<Order, ResultResourceSupport> {

    public OrderResourceAssembler() {
        super(OrderController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Order> instantiateResource(Order entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Order entity) {
        ResultResourceSupport resource = instantiateResource(entity);
        resource.add(linkTo(methodOn(OrderController.class).view(entity.getKey())).withSelfRel());
        resource.add(linkTo(methodOn(OrderController.class).payments(entity.getKey())).withRel("payments"));
        resource.add(linkTo(methodOn(OrderController.class).refunds(entity.getKey())).withRel("refunds"));
        resource.add(linkTo(methodOn(OrderController.class).items(entity.getKey())).withRel("items"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Order> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
