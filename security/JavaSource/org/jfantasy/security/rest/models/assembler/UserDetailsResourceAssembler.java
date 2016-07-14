package org.jfantasy.security.rest.models.assembler;

import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.security.bean.UserDetails;
import org.jfantasy.security.rest.UserController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserDetailsResourceAssembler extends ResourceAssemblerSupport<UserDetails, ResultResourceSupport> {

    public UserDetailsResourceAssembler() {
        super(UserController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<UserDetails> instantiateResource(UserDetails entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(UserDetails entity) {
        ResultResourceSupport resource = instantiateResource(entity);
        resource.add(linkTo(methodOn(UserController.class).profile(entity.getUserId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).view(entity.getUserId())).withRel("user"));
        return resource;
    }

}
