package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Invoice;
import org.jfantasy.member.rest.InviteController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class InvoiceResourceAssembler  extends ResourceAssemblerSupport<Invoice, ResultResourceSupport> {

    public InvoiceResourceAssembler() {
        super(InviteController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Invoice> instantiateResource(Invoice entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Invoice entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Invoice> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
