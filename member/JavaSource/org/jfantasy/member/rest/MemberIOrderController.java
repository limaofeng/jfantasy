package org.jfantasy.member.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.bean.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/** 开票订单 **/
@RestController
@RequestMapping(value = "/members/{id}/iorders", produces = {APPLICATION_JSON_VALUE})
public class MemberIOrderController {

    private final InvoiceOrderController controller;

    @Autowired
    public MemberIOrderController(InvoiceOrderController controller) {
        this.controller = controller;
    }

    @JsonResultFilter(
            allow = @AllowProperty(pojo = Member.class, name = {"id", "nick_name"})
    )
    /** 发票订单列表 **/
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(@PathVariable("id") Long id, Pager<InvoiceOrder> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id",id));
        filters.add(new PropertyFilter("EQE_status", InvoiceOrder.InvoiceOrderStatus.NONE));
        return controller.search(pager, filters);
    }

}
