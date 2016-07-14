package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.rest.WalletController;
import org.jfantasy.member.rest.models.PointDetails;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PointDetailsResourceAssembler extends ResourceAssemblerSupport<PointDetails, ResultResourceSupport> {


    public PointDetailsResourceAssembler() {
        super(WalletController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<PointDetails> instantiateResource(PointDetails entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(PointDetails entity) {
        ResultResourceSupport resource = instantiateResource(entity);
//        resource.add(linkTo(methodOn(WalletController.class).bills(entity.getId().toString(), new Pager<WalletBill>(), new ArrayList<PropertyFilter>())).withRel("bills"));
        return resource;
    }


}
