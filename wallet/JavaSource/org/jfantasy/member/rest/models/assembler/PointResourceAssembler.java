package org.jfantasy.member.rest.models.assembler;


import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Point;
import org.jfantasy.member.rest.WalletController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PointResourceAssembler extends ResourceAssemblerSupport<Point, ResultResourceSupport> {

    public PointResourceAssembler() {
        super(WalletController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Point> instantiateResource(Point entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Point entity) {
        return instantiateResource(entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Point> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }


}