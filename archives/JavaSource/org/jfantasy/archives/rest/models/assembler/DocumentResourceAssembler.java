package org.jfantasy.archives.rest.models.assembler;

import org.jfantasy.archives.bean.Document;
import org.jfantasy.archives.rest.DocumentController;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class DocumentResourceAssembler extends ResourceAssemblerSupport<Document, ResultResourceSupport> {

    public DocumentResourceAssembler() {
        super(DocumentController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Document> instantiateResource(Document entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Document entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Document> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}

