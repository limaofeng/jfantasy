package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.rest.models.assembler.CardDesignResourceAssembler;
import org.jfantasy.pay.service.CardDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "card-types", description = "会员卡设计")
@RestController
@RequestMapping("/card-designs")
public class CardDesignController {

    private CardDesignResourceAssembler assembler = new CardDesignResourceAssembler();

    @Autowired
    private CardDesignService cardDesignService;
    @Autowired
    private CardController cardController;
    @Autowired
    private CardBatchController cardBatchController;

    @ApiOperation("查询会员卡设计")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<CardDesign> pager, List<PropertyFilter> filters) {
        return assembler.toResources(cardDesignService.findPager(pager, filters));
    }

    @RequestMapping(value = "/{id}/cards", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> cards(@PathVariable("id") String id, Pager<Card> pager, ArrayList<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_design.key", id));
        return cardController.search(pager, filters);
    }

    @RequestMapping(value = "/{id}/batchs", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> batchs(@PathVariable("id") String id, Pager<CardBatch> pager, ArrayList<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_design.key", id));
        return cardBatchController.search(pager, filters);
    }

    @ApiOperation("会员卡设计详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }

    @ApiOperation("新增会员卡设计")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport view(@RequestBody CardDesign design) {
        return assembler.toResource(this.cardDesignService.save(design));
    }

    @ApiOperation("更新会员卡设计")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody CardDesign batch) {
        return assembler.toResource(this.cardDesignService.save(batch));
    }

    private CardDesign get(String id) {
        return this.cardDesignService.get(id);
    }

}
