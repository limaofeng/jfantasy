package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.rest.TransactionController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class TransactionResourceAssembler extends ResourceAssemblerSupport<Transaction, ResultResourceSupport> {

    public TransactionResourceAssembler() {
        super(TransactionController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Transaction> instantiateResource(Transaction entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Transaction entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getSn(), entity);
        resource.add(linkTo(methodOn(TransactionController.class).logs(entity.getSn())).withRel("logs"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Transaction> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
