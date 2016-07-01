package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.rest.WalletBillController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;


public class BillResourceAssembler extends ResourceAssemblerSupport<WalletBill, ResultResourceSupport> {

    public BillResourceAssembler() {
        super(WalletBillController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<WalletBill> instantiateResource(WalletBill entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(WalletBill entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
//        resource.add(linkTo(methodOn(WalletController.class).logs(entity.getSn())).withRel("logs"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<WalletBill> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
