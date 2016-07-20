package org.jfantasy.pay.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.rest.CardBatchController;
import org.jfantasy.pay.rest.CardDesignController;
import org.jfantasy.pay.rest.LogController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CardDesignResourceAssembler extends ResourceAssemblerSupport<CardDesign, ResultResourceSupport> {

    public CardDesignResourceAssembler() {
        super(CardDesignController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<CardDesign> instantiateResource(CardDesign entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(CardDesign entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getKey(), entity);
        resource.add(linkTo(methodOn(CardBatchController.class).cards(entity.getKey(), new Pager<Card>(), new ArrayList<PropertyFilter>())).withRel("cards"));
        resource.add(linkTo(methodOn(LogController.class).search(OwnerType.card_design, entity.getKey())).withRel("logs"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<CardDesign> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
