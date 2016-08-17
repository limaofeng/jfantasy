package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.rest.InvoiceOrderController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class InvoiceOrderResourceAssembler extends ResourceAssemblerSupport<InvoiceOrder, ResultResourceSupport> {

    public InvoiceOrderResourceAssembler() {
        super(InvoiceOrderController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<InvoiceOrder> instantiateResource(InvoiceOrder entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(InvoiceOrder entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<InvoiceOrder> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
