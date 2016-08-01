package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.rest.models.assembler.TeamMemberResourceAssembler;
import org.jfantasy.member.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "team", description = "团队成员")
@RestController
@RequestMapping("/team-members")
public class TeamMemberController {

    public final static TeamMemberResourceAssembler assembler = new TeamMemberResourceAssembler();

    @Autowired
    private TeamMemberService teamMemberService;

    @ApiOperation(value = "查看团队", notes = "查看团队")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "添加团队", notes = "添加团队")
    @RequestMapping(method = RequestMethod.POST)
    public ResultResourceSupport create(@RequestBody TeamMember member) {
        return assembler.toResource(this.teamMemberService.save(member));
    }

    @ApiOperation(value = "更新团队", notes = "更新团队地址")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResultResourceSupport update(@PathVariable("id") Long id, @RequestBody TeamMember member) {
        member.setId(id);
        return assembler.toResource(this.teamMemberService.update(member));
    }

    @ApiOperation(value = "删除团队", notes = "删除团队")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.teamMemberService.deltele(id);
    }

    private TeamMember get(Long id) {
        TeamMember member = this.teamMemberService.get(id);
        if (member == null) {
            throw new NotFoundException("[id =" + id + "]对应的团队成员信息不存在");
        }
        return member;
    }
}
