package org.jfantasy.archives.rest.models.assembler;

import org.jfantasy.archives.bean.Document;
import org.jfantasy.archives.bean.Record;
import org.jfantasy.archives.rest.RecordController;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class RecordResourceAssembler extends ResourceAssemblerSupport<Record, ResultResourceSupport> {

    public RecordResourceAssembler() {
        super(RecordController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Record> instantiateResource(Record entity) {
        ResultResourceSupport<Record> resource = new ResultResourceSupport<>(entity);
        for (Document document : entity.getDocuments()) {
            resource.set(document.getCode(), document.getData());
        }
        return resource;
    }

    @Override
    public ResultResourceSupport toResource(Record entity) {
        return createResourceWithId(entity.getId(), entity);
    }

    public Pager<ResultResourceSupport> toResources(Pager<Record> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }

}
