package org.jfantasy.security.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.rest.UserController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserResourceAssembler extends ResourceAssemblerSupport<User, ResultResourceSupport> {

    public UserResourceAssembler() {
        super(UserController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<User> instantiateResource(User entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(User entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(UserController.class).profile(entity.getId())).withRel("profile"));
        resource.add(linkTo(methodOn(UserController.class).menus(entity.getId())).withRel("menus"));
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<User> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
