package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.rest.MemberController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MemberResourceAssembler extends ResourceAssemblerSupport<Member, ResultResourceSupport> {

    public MemberResourceAssembler() {
        super(MemberController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Member> instantiateResource(Member entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Member entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(MemberController.class).comments(entity.getId(), new Pager<Comment>(), new ArrayList<PropertyFilter>())).withRel("comments"));
        resource.add(linkTo(methodOn(MemberController.class).receivers(entity.getId(), new ArrayList<PropertyFilter>())).withRel("receivers"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Member> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
