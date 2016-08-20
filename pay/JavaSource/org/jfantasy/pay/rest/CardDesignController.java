package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.pay.bean.*;
import org.jfantasy.pay.rest.models.LogForm;
import org.jfantasy.pay.rest.models.assembler.CardDesignResourceAssembler;
import org.jfantasy.pay.service.CardDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(value = "card-types", description = "会员卡设计")
@RestController
@RequestMapping("/card-designs")
public class CardDesignController {

    private CardDesignResourceAssembler assembler = new CardDesignResourceAssembler();

    private final CardDesignService cardDesignService;
    private final CardController cardController;
    private final CardBatchController cardBatchController;

    @Autowired
    public CardDesignController(CardDesignService cardDesignService, CardController cardController, CardBatchController cardBatchController) {
        this.cardDesignService = cardDesignService;
        this.cardController = cardController;
        this.cardBatchController = cardBatchController;
    }

    @JsonResultFilter(
            ignore = {
                    @IgnoreProperty(pojo = CardDesign.class, name = {"logs"})
            },
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("查询会员卡设计")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<CardDesign> pager, List<PropertyFilter> filters) {
        return assembler.toResources(cardDesignService.findPager(pager, filters));
    }

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"}),
                    @AllowProperty(pojo = Log.class, name = {"status", "notes", "logTime"})
            }
    )
    @ApiOperation("会员卡设计详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }


    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("添加卡设计")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@Validated(RESTful.POST.class) @RequestBody CardDesign design) {
        return assembler.toResource(this.cardDesignService.save(design));
    }

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("更新会员卡设计")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultResourceSupport update(@PathVariable("id") String id, HttpServletRequest request, @RequestBody CardDesign design) {
        design.setKey(id);
        return assembler.toResource(this.cardDesignService.update(design, RequestMethod.PATCH.name().equalsIgnoreCase(request.getMethod())));
    }

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("发布会员卡设计")
    @RequestMapping(value = "/{id}/publish", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport publish(@PathVariable("id") String id, @RequestBody LogForm form) {
        return assembler.toResource(this.cardDesignService.publish(id, form.getNotes()));
    }

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("取消发布会员卡设计")
    @RequestMapping(value = "/{id}/unpublish", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport unpublish(@PathVariable("id") String id, @RequestBody LogForm form) {
        return assembler.toResource(this.cardDesignService.unpublish(id, form.getNotes()));
    }

    @JsonResultFilter(
            allow = {
                    @AllowProperty(pojo = CardType.class, name = {"key", "name"})
            }
    )
    @ApiOperation("销毁会员卡设计")
    @RequestMapping(value = "/{id}/destroy", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport destroy(@PathVariable("id") String id, @RequestBody LogForm form) {
        return assembler.toResource(this.cardDesignService.destroy(id, form.getNotes()));
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

    private CardDesign get(String id) {
        return this.cardDesignService.get(id);
    }

}
