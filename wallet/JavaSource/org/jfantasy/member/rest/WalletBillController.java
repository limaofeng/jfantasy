package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.rest.models.assembler.BillResourceAssembler;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "bills", description = "账单")
@RestController
@RequestMapping("/bills")
public class WalletBillController {

    private BillResourceAssembler assembler = new BillResourceAssembler();

    @Autowired
    private WalletService walletService;

    @JsonResultFilter(allow = @AllowProperty(pojo = Wallet.class, name = {"id"}))
    @ApiOperation(value = "获取账单信息", notes = "返回账单详情")
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<WalletBill> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.walletService.findBillPager(pager, filters));
    }

    @ApiOperation(value = "获取账单信息", notes = "返回账单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.walletService.getBill(id));
    }

}
