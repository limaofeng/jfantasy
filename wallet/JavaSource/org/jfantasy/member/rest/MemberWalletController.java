package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Point;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.rest.models.PointDetails;
import org.jfantasy.member.rest.models.assembler.PointDetailsResourceAssembler;
import org.jfantasy.member.rest.models.assembler.PointResourceAssembler;
import org.jfantasy.member.rest.models.assembler.WalletResourceAssembler;
import org.jfantasy.member.service.CardService;
import org.jfantasy.member.service.PointService;
import org.jfantasy.member.service.WalletService;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.jfantasy.oauth.userdetails.enums.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "wallets", description = "钱包接口")
@RestController
public class MemberWalletController {

    private PointDetailsResourceAssembler assembler = new PointDetailsResourceAssembler();
    private PointResourceAssembler pointAssembler = new PointResourceAssembler();
    private WalletResourceAssembler walletAssembler = new WalletResourceAssembler();

    @Autowired
    private WalletService walletService;
    @Autowired
    private PointService pointService;
    @Autowired
    private WalletController walletController;
    @Autowired
    private CardService cardService;

    @ApiOperation(
            value = "用户钱包信息",
            notes = "必须通过用户授权访问",
            authorizations =
            @Authorization(value = "MEMBER",
                    scopes = @AuthorizationScope(scope = "member", description = "会员")
            )
    )
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public ResultResourceSupport wallet() {
        OAuthUserDetails user = SpringSecurityUtils.getCurrentUser(OAuthUserDetails.class);
        if (user == null || user.getScope() != Scope.member) {
            throw new RestException(401, "没有权限访问接口");
        }
        return walletAssembler.toResource(walletService.getWalletByMember(user.getId()));
    }

    @ApiOperation(
            value = "用户创建钱包",
            notes = "必须通过用户授权访问",
            authorizations =
            @Authorization(value = "MEMBER",
                    scopes = @AuthorizationScope(scope = "member", description = "会员")
            )
    )
    @RequestMapping(value = "/wallet", method = RequestMethod.POST)
    public ResultResourceSupport save() {
        OAuthUserDetails user = SpringSecurityUtils.getCurrentUser(OAuthUserDetails.class);
        if (user == null || user.getScope() != Scope.member) {
            throw new RestException(401, "没有权限访问接口");
        }
        return walletAssembler.toResource(walletService.save(user.getId()));
    }

    @ApiOperation(value = "用户积分信息")
    @RequestMapping(value = "/point-details", method = RequestMethod.GET)
    public ResultResourceSupport pointDetails() {
        OAuthUserDetails user = SpringSecurityUtils.getCurrentUser(OAuthUserDetails.class);
        if (user == null || user.getScope() != Scope.member) {
            throw new RestException(401, "没有权限访问接口");
        }
        PointDetails details = new PointDetails();
        Wallet wallet = walletService.getWalletByMember(user.getId());
        details.setPoints(wallet.getPoints());
        return assembler.toResource(details);
    }

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Wallet.class, name = {"member", "bills"}))
    @ApiOperation(value = "用户钱包信息", notes = "返回钱包详情")
    @RequestMapping(value = "/members/{memid}/wallet", method = RequestMethod.GET)
    public ResultResourceSupport _view(@PathVariable("memid") Long id) {
        Wallet wallet = walletService.getWalletByMember(id);
        ResultResourceSupport resource = walletController.view(wallet.getId());
        if (Member.MEMBER_TYPE_PERSONAL.equals(wallet.getMember().getType())) {
            resource.set("level", wallet.getMember().getDetails().getLevel());
        }
        resource.set("cards", cardService.count(wallet.getId()));
        return resource;
    }

    @ApiOperation(value = "用户积分信息")
    @RequestMapping(value = "/members/{memid}/point-details", method = RequestMethod.GET)
    public ResultResourceSupport _pointDetails(@PathVariable("memid") Long id) {
        PointDetails details = new PointDetails();
        Wallet wallet = walletService.getWalletByMember(id);
        details.setPoints(wallet.getPoints());
        return assembler.toResource(details);
    }

    @ApiOperation(value = "用户积分列表")
    @RequestMapping(value = "/members/{memid}/points", method = RequestMethod.GET)
    public Pager<ResultResourceSupport> _points(@PathVariable("memid") Long id, Pager<Point> pager, List<PropertyFilter> filters) {
        Wallet wallet = walletService.getWalletByMember(id);
        filters.add(new PropertyFilter("EQL_wallet.id", wallet.getId().toString()));
        return pointAssembler.toResources(pointService.findPager(pager, filters));
    }

}
