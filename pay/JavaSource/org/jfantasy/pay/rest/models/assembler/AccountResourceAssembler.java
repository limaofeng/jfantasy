package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Point;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.rest.AccountController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class AccountResourceAssembler extends ResourceAssemblerSupport<Account, ResultResourceSupport> {

    public AccountResourceAssembler() {
        super(AccountController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Account> instantiateResource(Account entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Account entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getSn(), entity);
        resource.add(linkTo(methodOn(AccountController.class).transactions(entity.getSn(), new Pager<Transaction>(), new ArrayList<PropertyFilter>())).withRel("transactions"));
        resource.add(linkTo(methodOn(AccountController.class).points(entity.getSn(), new Pager<Point>(), new ArrayList<PropertyFilter>())).withRel("points"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Account> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
