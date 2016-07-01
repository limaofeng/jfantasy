package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Receiver;
import org.jfantasy.member.rest.ReceiverController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class ReceiverResourceAssembler extends ResourceAssemblerSupport<Receiver, ResultResourceSupport> {

    public ReceiverResourceAssembler() {
        super(ReceiverController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Receiver> instantiateResource(Receiver entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Receiver entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Receiver> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}