package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.rest.TeamMemberController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class TeamMemberResourceAssembler extends ResourceAssemblerSupport<TeamMember, ResultResourceSupport> {


    public TeamMemberResourceAssembler() {
        super(TeamMemberController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<TeamMember> instantiateResource(TeamMember entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(TeamMember entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<TeamMember> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
