package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.rest.models.assembler.WalletResourceAssembler;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "wallets", description = "钱包接口")
@RestController
@RequestMapping("/wallets")
public class WalletController {

    private WalletResourceAssembler assembler = new WalletResourceAssembler();

    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletBillController walletBillController;

    @JsonIgnoreProperties(allow = @AllowProperty(pojo = Member.class, name = {"id", "username", "nickName"}))
    @ApiOperation(value = "钱包列表", notes = "查询所有的钱包")
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<Wallet> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.walletService.findPager(pager, filters));
    }

    @ApiOperation(value = "获取钱包信息", notes = "返回钱包详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.walletService.getWallet(id));
    }

    @ApiOperation(value = "查询钱包中的账单信息", notes = "账单列表")
    @RequestMapping(value = "/{walletid}/bills", method = RequestMethod.GET)
    public Pager<ResultResourceSupport> bills(@PathVariable("walletid") String walletId, Pager<WalletBill> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_wallet.id", walletId));
        return walletBillController.search(pager, filters);
    }

}
