package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.MemberDetails;
import org.jfantasy.member.rest.MemberController;
import org.jfantasy.security.rest.UserController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ProfileResourceAssembler extends ResourceAssemblerSupport<MemberDetails, ResultResourceSupport> {

    public ProfileResourceAssembler() {
        super(MemberController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<MemberDetails> instantiateResource(MemberDetails entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(MemberDetails entity) {
        ResultResourceSupport resource = instantiateResource(entity);
        resource.add(linkTo(methodOn(UserController.class).profile(entity.getMemberId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).view(entity.getMemberId())).withRel("member"));
        return resource;
    }

}
