package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Invite;
import org.jfantasy.member.rest.InviteController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class InviteResourceAssembler extends ResourceAssemblerSupport<Invite, ResultResourceSupport> {

    public InviteResourceAssembler() {
        super(InviteController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Invite> instantiateResource(Invite entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Invite entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Invite> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
