package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Level;
import org.jfantasy.member.rest.LevelController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class LevelResourceAssembler extends ResourceAssemblerSupport<Level, ResultResourceSupport> {

    public LevelResourceAssembler() {
        super(LevelController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Level> instantiateResource(Level entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Level entity) {
        return instantiateResource(entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Level> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
