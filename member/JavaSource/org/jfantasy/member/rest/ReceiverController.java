package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Receiver;
import org.jfantasy.member.rest.models.assembler.ReceiverResourceAssembler;
import org.jfantasy.member.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "receiver", description = "收货地址")
@RestController
@RequestMapping("/receivers")
public class ReceiverController {

    private ReceiverResourceAssembler assembler = new ReceiverResourceAssembler();

    @Autowired
    private ReceiverService receiverService;

    @ApiOperation(value = "查询收货地址", notes = "返回会员的收货地址")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> search(List<PropertyFilter> filters) {
        return assembler.toResources(this.receiverService.find(filters, "isDefault", "asc"));
    }

    @ApiOperation(value = "添加收货地址", notes = "添加会员收货地址")
    @RequestMapping(method = RequestMethod.POST)
    public ResultResourceSupport create(@RequestBody Receiver receiver) {
        return assembler.toResource(this.receiverService.save(receiver));
    }

    @ApiOperation(value = "更新收货地址", notes = "更新会员的收货地址")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResultResourceSupport update(@PathVariable("id") Long id, @RequestBody Receiver receiver) {
        Receiver _receiver = this.receiverService.get(id);
        if (_receiver == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        receiver.setId(id);
        return assembler.toResource(this.receiverService.save(receiver));
    }

    @ApiOperation(value = "删除收货地址", notes = "删除会员收货地址")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        Receiver receiver = this.receiverService.get(id);
        if (receiver == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        this.receiverService.deltele(id);
    }

}
