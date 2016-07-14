package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Point;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.rest.models.PointDetails;
import org.jfantasy.member.rest.models.assembler.PointDetailsResourceAssembler;
import org.jfantasy.member.rest.models.assembler.PointResourceAssembler;
import org.jfantasy.member.service.PointService;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "wallets", description = "钱包接口")
@RestController
@RequestMapping("/members")
public class MemberWalletController {

    private PointDetailsResourceAssembler assembler = new PointDetailsResourceAssembler();
    private PointResourceAssembler pointAssembler = new PointResourceAssembler();

    @Autowired
    private WalletService walletService;
    @Autowired
    private PointService pointService;
    @Autowired
    private WalletController walletController;

    @ApiOperation(value = "用户钱包信息", notes = "返回钱包详情")
    @RequestMapping(value = "/{memid}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("memid") Long id) {
        Wallet wallet = walletService.getWalletByMember(id);
        return walletController.view(wallet.getId());
    }

    @ApiOperation(value = "用户积分信息")
    @RequestMapping(value = "/{memid}/point-details", method = RequestMethod.GET)
    public ResultResourceSupport pointDetails(@PathVariable("memid") Long id) {
        PointDetails details = new PointDetails();
        Wallet wallet = walletService.getWalletByMember(id);
        details.setPoints(wallet.getPoints());
        return assembler.toResource(details);
    }

    @ApiOperation(value = "用户积分列表")
    @RequestMapping(value = "/{memid}/points", method = RequestMethod.GET)
    public Pager<ResultResourceSupport> points(@PathVariable("memid") Long id, Pager<Point> pager, List<PropertyFilter> filters) {
        Wallet wallet = walletService.getWalletByMember(id);
        filters.add(new PropertyFilter("EQL_wallet.id",wallet.getId().toString()));
        return pointAssembler.toResources(pointService.findPager(pager, filters));
    }

}
