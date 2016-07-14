package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.rest.models.assembler.CardBatchResourceAssembler;
import org.jfantasy.pay.service.CardBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "card-batch", description = "卡发行批次")
@RestController
@RequestMapping("/card-batchs")
public class CardBatchController {

    private CardBatchResourceAssembler assembler = new CardBatchResourceAssembler();

    @Autowired
    private CardBatchService cardBatchService;
    @Autowired
    private CardController cardController;

    @ApiOperation("查询卡发行批次")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<CardBatch> pager, List<PropertyFilter> filters) {
        return assembler.toResources(cardBatchService.findPager(pager, filters));
    }

    @RequestMapping(value = "/{id}/cards", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> cards(@PathVariable("id") String id, Pager<Card> pager, ArrayList<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_batch.no", id));
        return cardController.search(pager, filters);
    }

    @ApiOperation("批次详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }

    @ApiOperation("新增批次")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport view(@RequestBody CardBatch batch) {
        return assembler.toResource(this.cardBatchService.save(batch));
    }

    @ApiOperation("更新批次")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody CardBatch batch) {
        return assembler.toResource(this.cardBatchService.update(batch));
    }

    private CardBatch get(String id) {
        return cardBatchService.get(id);
    }

}
