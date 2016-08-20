package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardType;
import org.jfantasy.pay.rest.models.assembler.CardTypeResourceAssembler;
import org.jfantasy.pay.service.CardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "card-types", description = "会员卡类型")
@RestController
@RequestMapping("/card-types")
public class CardTypeController {

    public static CardTypeResourceAssembler assembler = new CardTypeResourceAssembler();

    private final CardTypeService cardTypeService;
    @Autowired
    private CardBatchController cardBatchController;
    @Autowired
    private CardController cardController;

    @Autowired
    public CardTypeController(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    @ApiOperation("全部会员卡类型")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<CardType> pager, List<PropertyFilter> filters) {
        return assembler.toResources(cardTypeService.findPager(pager, filters));
    }

    @ApiOperation("全部会员卡类型详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }

    @ApiOperation("添加会员卡类型")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@Validated(RESTful.POST.class) @RequestBody CardType type) {
        return assembler.toResource(this.cardTypeService.save(type));
    }

    @ApiOperation("修改会员卡类型")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody CardType type) {
        type.setKey(id);
        return assembler.toResource(this.cardTypeService.save(type));
    }

    @ApiOperation("删除会员卡类型")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        this.cardTypeService.delete(id);
    }

    /**
     * 会员卡类型对应的发行批次
     *
     * @param id      卡类型编码
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return Pager
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/batchs")
    @ResponseBody
    public Pager<ResultResourceSupport> batchs(@PathVariable("id") String id, Pager<CardBatch> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_project.Key", id));
        return cardBatchController.search(pager, filters);
    }

    /**
     * 会员卡类型下的
     *
     * @param id      卡类型编码
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return Pager
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/cards")
    @ResponseBody
    public Pager<ResultResourceSupport> cards(@PathVariable("id") String id, Pager<Card> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_type.key", id));
        return cardController.search(pager, filters);
    }

    private CardType get(String id) {
        return this.cardTypeService.get(id);
    }

}
