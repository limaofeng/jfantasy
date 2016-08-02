package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.rest.FavoriteController;
import org.jfantasy.member.rest.MemberController;
import org.jfantasy.member.rest.MemberTagController;
import org.springframework.hateoas.Link;
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
        resource.add(linkTo(methodOn(MemberController.class).favorites(entity.getId(), "{type}")).withRel("favorites"));
        resource.add(new Link("/members/" + entity.getId() + "/level", "level"));
        resource.add(linkTo(methodOn(MemberTagController.class).tags(entity.getId().toString(), "{type}")).withRel("tags"));
        resource.add(linkTo(methodOn(FavoriteController.class).get(entity.getId(), "{type}", "{target_type}", "{target_id}")).withRel("favorite/watch"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Member> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
