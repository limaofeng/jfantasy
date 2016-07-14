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
import org.jfantasy.pay.rest.models.LogForm;
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

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"}),
                    @AllowProperty(pojo = CardDesign.class, name = {"key", "name", "amount", "extras"})
            }
    )
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

    @ApiOperation("制作卡")
    @RequestMapping(value = "/{id}/make", method = RequestMethod.POST)
    @ResponseBody
    public List<ResultResourceSupport> make(@PathVariable("id") String id, @RequestBody LogForm form) {
        return CardController.assembler.toResources(this.cardBatchService.make(id, form.getNotes()));
    }

    @ApiOperation("正式发行卡")
    @RequestMapping(value = "/{id}/release", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport release(@PathVariable("id") String id, @RequestBody LogForm form) {
        return assembler.toResource(this.cardBatchService.release(id, form.getNotes()));
    }

    @ApiOperation("取消发行")
    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport cancel(@PathVariable("id") String id, @RequestBody LogForm form) {
        return assembler.toResource(this.cardBatchService.cancel(id, form.getNotes()));
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
