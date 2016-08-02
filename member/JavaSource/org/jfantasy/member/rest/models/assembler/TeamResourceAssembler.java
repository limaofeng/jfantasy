package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Invite;
import org.jfantasy.member.bean.Team;
import org.jfantasy.member.rest.TeamController;
import org.jfantasy.member.rest.TeamTagController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class TeamResourceAssembler extends ResourceAssemblerSupport<Team, ResultResourceSupport> {

    public TeamResourceAssembler() {
        super(TeamController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Team> instantiateResource(Team entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Team entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getKey(), entity);
        resource.add(linkTo(methodOn(TeamController.class).invites(entity.getKey(), new Pager<Invite>(), new ArrayList<PropertyFilter>())).withRel("invites"));
        resource.add(linkTo(methodOn(TeamTagController.class).tags(entity.getKey(), "")).withRel("tags"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Team> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
