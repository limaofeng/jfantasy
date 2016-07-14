package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.CardType;
import org.jfantasy.pay.rest.models.assembler.CardResourceAssembler;
import org.jfantasy.pay.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "cards", description = "会员卡")
@RestController
@RequestMapping("/cards")
public class CardController {

    public static CardResourceAssembler assembler = new CardResourceAssembler();

    @Autowired
    private CardService cardService;

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"}),
                    @AllowProperty(pojo = CardDesign.class, name = {"key", "name"}),
                    @AllowProperty(pojo = CardBatch.class, name = {"no"})
            }
    )
    @ApiOperation("查询卡列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Card> pager, List<PropertyFilter> filters) {
        return assembler.toResources(cardService.findPager(pager, filters));
    }

    @ApiOperation("账户详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }

    private Card get(String id) {
        return cardService.get(id);
    }

}
