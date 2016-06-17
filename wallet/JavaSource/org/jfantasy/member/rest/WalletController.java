package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
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

    @Autowired
    private WalletService walletService;

    @ApiOperation(value = "获取钱包信息", notes = "返回钱包详情")
    @RequestMapping(value = "/{memid}", method = RequestMethod.GET)
    public Wallet view(@PathVariable("memid") Long memberId) {
        return this.walletService.getWallet(memberId);
    }

    @ApiOperation(value = "查询钱包中的账单信息", notes = "账单列表")
    @RequestMapping(value = "/{walletid}/bills", method = RequestMethod.GET)
    public Pager<WalletBill> bills(@PathVariable("walletid") String walletId, Pager<WalletBill> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_wallet.id", walletId));
        return this.walletService.findPager(pager, filters);
    }

}
