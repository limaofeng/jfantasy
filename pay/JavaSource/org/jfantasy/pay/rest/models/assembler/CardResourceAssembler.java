package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.rest.*;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CardResourceAssembler extends ResourceAssemblerSupport<Card, ResultResourceSupport> {

    public CardResourceAssembler() {
        super(AccountController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Card> instantiateResource(Card entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Card entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getNo(), entity);
        resource.add(linkTo(methodOn(CardBatchController.class).view(entity.getBatch().getId())).withRel("batch"));
        resource.add(linkTo(methodOn(CardTypeController.class).view(entity.getType().getKey())).withRel("type"));
        resource.add(linkTo(methodOn(CardDesignController.class).view(entity.getDesign().getKey())).withRel("design"));
        resource.add(linkTo(methodOn(LogController.class).search(OwnerType.card_batch, entity.getNo())).withRel("logs"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Card> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
