package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
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
    @Autowired
    private CardBatchController cardBatchController;

    @ApiOperation("查询账户")
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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/batch")
    @ResponseBody
    public ResultResourceSupport batch(@PathVariable("id") String id) {
        return cardBatchController.view(get(id).getBatch().getNo());
    }

}
