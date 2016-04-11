package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.ForbiddenException;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Receiver;
import org.jfantasy.member.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@Api(value = "members-receiver", description = "收货地址信息")
@RestController
@RequestMapping("/members")
public class ReceiverController {

    @Autowired
    private ReceiverService receiverService;

    @ApiOperation(value = "查询收货地址", notes = "返回会员的收货地址")
    @RequestMapping(value = "/{memid}/receivers", method = RequestMethod.GET)
    @ResponseBody
    public List<Receiver> search(@PathVariable("memid") Long memberId, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_member.id", memberId.toString()));
        return this.receiverService.find(filters, "isDefault", "asc");
    }

    @ApiOperation(value = "添加收货地址", notes = "添加会员收货地址")
    @RequestMapping(value = "/{memid}/receivers", method = RequestMethod.POST)
    public Receiver create(@PathVariable("memid") Long memberId, @RequestBody Receiver receiver) {
        receiver.setMember(new Member(memberId));
        return this.receiverService.save(receiver);
    }

    @ApiOperation(value = "更新收货地址", notes = "更新会员的收货地址")
    @RequestMapping(value = "/{memid}/receivers/{id}", method = RequestMethod.PUT)
    public Receiver update(@PathVariable("memid") Long memberId, @PathVariable("id") Long id, @RequestBody Receiver receiver) {
        Receiver _receiver = this.receiverService.get(id);
        if (_receiver == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        if (!memberId.equals(_receiver.getMember().getId())) {
            throw new ForbiddenException("[memid=" + memberId + "]不能修改该条收货记录信息,因为它不属于该会员");
        }
        receiver.setId(id);
        receiver.setMember(new Member(memberId));
        return this.receiverService.save(receiver);
    }

    @ApiOperation(value = "删除收货地址", notes = "删除会员收货地址")
    @RequestMapping(value = "/{memid}/receivers/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("memid") Long memberId, @PathVariable("id") Long id) {
        Receiver receiver = this.receiverService.get(id);
        if (receiver == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        if (!memberId.equals(receiver.getMember().getId())) {
            throw new ForbiddenException("[memid=" + memberId + "]不能修改该条收货记录信息,因为它不属于该会员");
        }
        this.receiverService.deltele(id);
    }

}
