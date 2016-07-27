package org.jfantasy.archives.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.archives.bean.Document;
import org.jfantasy.archives.rest.models.assembler.DocumentResourceAssembler;
import org.jfantasy.archives.service.DocumentService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "documents", description = "文档/文件信息")
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private DocumentResourceAssembler assembler = new DocumentResourceAssembler();

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "人员信息列表", notes = "人员信息列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Document> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.documentService.findPager(pager, filters));
    }

    @ApiOperation(value = "查看人员信息", notes = "查看人员信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "添加人员信息", notes = "添加人员信息")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResourceSupport create(@RequestBody Document document) {
        return assembler.toResource(this.documentService.save(document));
    }

    @ApiOperation(value = "更新人员信息", notes = "更新人员信息地址")
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResultResourceSupport update(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Document document) {
        document.setId(id);
        return assembler.toResource(this.documentService.update(document, WebUtil.has(request, RequestMethod.PATCH)));
    }

    @ApiOperation(value = "删除人员信息", notes = "删除人员信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.documentService.deltele(id);
    }

    private Document get(Long id) {
        return this.documentService.get(id);
    }

}
