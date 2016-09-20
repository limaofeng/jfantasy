package org.jfantasy.member.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Invoice;
import org.jfantasy.member.bean.InvoiceItem;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.bean.Team;
import org.jfantasy.member.rest.models.assembler.InvoiceResourceAssembler;
import org.jfantasy.member.service.InvoiceService;
import org.jfantasy.member.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 发票 **/
@RestController
@RequestMapping(value = "/teams/{id}/invoices")
public class TeamInvoiceController {

    protected static InvoiceResourceAssembler assembler = new InvoiceResourceAssembler();

    private final TeamService teamService;
    private final InvoiceService invoiceService;

    @Autowired
    public TeamInvoiceController(InvoiceService invoiceService, TeamService teamService) {
        this.invoiceService = invoiceService;
        this.teamService = teamService;
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = InvoiceItem.class, name = {"order_id"}),
            allow = @AllowProperty(pojo = InvoiceOrder.class, name = {"order_sn", "order_type", "name"})
    )
    /** 集团的发票申请列表 **/
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(@PathVariable("id") String teamId, Pager<Invoice> pager, List<PropertyFilter> filters) {
        Team team = this.teamService.get(teamId);
        if (team == null) {
            throw new NotFoundException("[teamid = " + teamId + "]不存在");
        }
        filters.add(new PropertyFilter("EQS_targetType", "team"));
        filters.add(new PropertyFilter("EQS_targetId", teamId));
        return assembler.toResources(this.invoiceService.findPager(pager, filters));
    }

}
