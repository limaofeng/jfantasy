package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.rest.WalletController;
import org.jfantasy.member.rest.models.WalletResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class WalletResourceAssembler extends ResourceAssemblerSupport<Wallet, WalletResource> {

    public WalletResourceAssembler() {
        super(WalletController.class, WalletResource.class);
    }

    @Override
    protected WalletResource instantiateResource(Wallet entity) {
        return new WalletResource(entity);
    }

    @Override
    public WalletResource toResource(Wallet entity) {
        WalletResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(WalletController.class).bills(entity.getId().toString(), new Pager<WalletBill>(), new ArrayList<PropertyFilter>())).withRel("bills"));
        return resource;
    }

    public Pager<WalletResource> toResources(Pager<Wallet> pager) {
        Pager<WalletResource> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
