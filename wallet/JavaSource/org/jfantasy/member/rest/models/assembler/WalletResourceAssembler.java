package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.rest.WalletController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class WalletResourceAssembler extends ResourceAssemblerSupport<Wallet, ResultResourceSupport> {

    public WalletResourceAssembler() {
        super(WalletController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Wallet> instantiateResource(Wallet entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Wallet entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(WalletController.class).bills(entity.getId().toString(), new Pager<WalletBill>(), new ArrayList<PropertyFilter>())).withRel("bills"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Wallet> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
