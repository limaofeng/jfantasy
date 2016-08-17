package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.member.bean.Invoice;
import org.jfantasy.member.bean.InvoiceItem;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.rest.models.InvoiceForm;
import org.jfantasy.member.rest.models.assembler.InvoiceResourceAssembler;
import org.jfantasy.member.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "invoices", description = "发票")
@RestController
@RequestMapping(value = "/invoices", produces = {APPLICATION_JSON_VALUE})
public class InvoiceController {

    protected static InvoiceResourceAssembler assembler = new InvoiceResourceAssembler();

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = InvoiceItem.class, name = {"order_id"}),
            allow = @AllowProperty(pojo = InvoiceOrder.class, name = {"order_sn", "order_type", "name"})
    )
    @ApiOperation(value = "发票列表")
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<Invoice> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.invoiceService.findPager(pager, filters));
    }

    @ApiOperation(value = "添加发票")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResourceSupport create(@Validated(RESTful.POST.class) @RequestBody Invoice invoice) {
        return assembler.toResource(this.invoiceService.save(invoice));
    }

    @JsonResultFilter(
            ignore = {
                    @IgnoreProperty(pojo = InvoiceItem.class, name = {"order_id"}),
                    @IgnoreProperty(pojo = InvoiceOrder.class, name = {"id", "creator", "createTime", "modifier", "modifyTime"})
            }
    )
    @ApiOperation(value = "查看发票")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "更新发票", notes = "主要用于发票开出后,更新发票的信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResultResourceSupport update(@PathVariable("id") Long id, @Validated(RESTful.PATCH.class) @RequestBody InvoiceForm form) {
        return assembler.toResource(this.invoiceService.update(BeanUtil.copyProperties(new Invoice(id), form)));
    }

    @ApiOperation(value = "删除发票")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.invoiceService.deltele(id);
    }

    private Invoice get(Long id) {
        Invoice invoice = this.invoiceService.get(id);
        if (invoice == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        return invoice;
    }

}
