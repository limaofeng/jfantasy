package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.InvoiceItem;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.rest.models.assembler.InvoiceOrderResourceAssembler;
import org.jfantasy.member.service.InvoiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "invoice-orders", description = "开票订单")
@RestController
@RequestMapping(value = "/iorders", produces = {APPLICATION_JSON_VALUE})
public class InvoiceOrderController {

    protected static InvoiceOrderResourceAssembler assembler = new InvoiceOrderResourceAssembler();

    private final InvoiceOrderService invoiceOrderService;

    @Autowired
    public InvoiceOrderController(InvoiceOrderService invoiceOrderService) {
        this.invoiceOrderService = invoiceOrderService;
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = InvoiceItem.class, name = {"order_id"}),
            allow = @AllowProperty(pojo = InvoiceOrder.class, name = {"order_sn", "order_type", "name"})
    )
    @ApiOperation(value = "发票列表")
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<InvoiceOrder> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.invoiceOrderService.findPager(pager, filters));
    }

}
