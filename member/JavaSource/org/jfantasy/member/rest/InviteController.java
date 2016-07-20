package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Invite;
import org.jfantasy.member.rest.models.assembler.InviteResourceAssembler;
import org.jfantasy.member.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "invite", description = "邀请")
@RestController
@RequestMapping("/invites")
public class InviteController {

    protected static InviteResourceAssembler assembler = new InviteResourceAssembler();

    @Autowired
    private InviteService inviteService;

    @ApiOperation(value = "发起邀请", notes = "发起邀请")
    @RequestMapping(method = RequestMethod.POST)
    public ResultResourceSupport create(@RequestBody Invite invite) {
        return assembler.toResource(this.inviteService.save(invite));
    }

    @ApiOperation(value = "查看邀请", notes = "查看邀请")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "删除邀请", notes = "删除邀请")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.inviteService.deltele(id);
    }

    private Invite get(Long id) {
        Invite invite = this.inviteService.get(id);
        if (invite == null) {
            throw new NotFoundException("[id =" + id + "]对应的收货信息不存在");
        }
        return invite;
    }

}
