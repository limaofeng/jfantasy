package org.jfantasy.member.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.rest.models.assembler.InvoiceOrderResourceAssembler;
import org.jfantasy.member.service.InvoiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 开票订单
 **/
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
            allow = @AllowProperty(pojo = Member.class, name = {"id", "nick_name"})
    )
    /** 发票订单列表 **/
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<InvoiceOrder> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.invoiceOrderService.findPager(pager, filters));
    }

    @JsonResultFilter(
            allow = @AllowProperty(pojo = Member.class, name = {"id", "nick_name"})
    )
    /** 添加发票接口 **/
    @RequestMapping(method = RequestMethod.POST)
    public ResultResourceSupport save(@RequestBody InvoiceOrder order) {
        return assembler.toResource(this.invoiceOrderService.save(order));
    }

}
