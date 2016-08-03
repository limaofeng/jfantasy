package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Level;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.rest.models.assembler.LevelResourceAssembler;
import org.jfantasy.member.service.LevelService;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.member.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "level", description = "会员等级")
@RestController
public class LevelController {

    private LevelResourceAssembler assembler = new LevelResourceAssembler();

    @Autowired
    private LevelService levelService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private MemberService memberService;

    @JsonResultFilter(List.class)
    @ApiOperation(value = "会员等级")
    @RequestMapping(value = "/levels", method = RequestMethod.GET)
    public Pager<ResultResourceSupport> search(Pager<Level> pager, List<PropertyFilter> filters) {
        return assembler.toResources(levelService.search(pager, filters));
    }

    @ApiOperation(value = "添加等级")
    @RequestMapping(value = "/levels", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@RequestBody Level level) {
        return assembler.toResource(levelService.save(level));
    }

    @ApiOperation(value = "修改等级")
    @RequestMapping(value = "/levels/{id}", method = RequestMethod.PATCH)
    public ResultResourceSupport update(@PathVariable("id") Long id, @RequestBody Level level) {
        level.setId(id);
        return assembler.toResource(levelService.update(level));
    }

    @ApiOperation(value = "删除等级")
    @RequestMapping(value = "/levels/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id) {
        levelService.delete(id);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = BaseBusEntity.class, name = {""}))
    @ApiOperation(value = "用户的会员等级")
    @RequestMapping(value = "/members/{memid}/level", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport level(@PathVariable("memid") Long id) {
        Member member = this.memberService.get(id);
        Wallet wallet = this.walletService.getWalletByMember(id);
        ResultResourceSupport resource = assembler.toResource(levelService.get(member.getDetails().getLevel()));
        resource.set("growth", wallet.getGrowth());//设置用户成长值
        return resource;
    }

}
