package org.jfantasy.pay.rest.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.rest.PayConfigController;
import org.jfantasy.pay.rest.results.PayConfigResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PayConfigResourceAssembler extends ResourceAssemblerSupport<PayConfig, PayConfigResource> {

    public PayConfigResourceAssembler() {
        super(PayConfigController.class, PayConfigResource.class);
    }

    @Override
    protected PayConfigResource instantiateResource(PayConfig entity) {
        return new PayConfigResource(entity);
    }

    @Override
    public PayConfigResource toResource(PayConfig entity) {
        PayConfigResource resource = createResourceWithId(entity.getId(),entity);
        resource.add();
        return resource;
    }

    public Pager<PayConfigResource> toResources(Pager<PayConfig> pager) {
        Pager<PayConfigResource> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}
