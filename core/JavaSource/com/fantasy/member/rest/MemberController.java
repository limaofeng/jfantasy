package com.fantasy.member.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping(method = RequestMethod.GET)
    public Pager<Member> search(Pager<Member> pager, List<PropertyFilter> filters) {
        return this.memberService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{username}/login", method = RequestMethod.POST)
    @ResponseBody
    public Member login(@PathVariable("username") String username, @RequestBody String password) {
        return this.memberService.login(username, password);
    }

    @RequestMapping(value = "/{username}/register", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable("username") String username) {
        this.memberService.logout(username);
    }

    @RequestMapping(value = "/{username}/register", method = RequestMethod.POST)
    @ResponseBody
    public Member register(@PathVariable("username") String username, @RequestBody Member member) {
        member.setUsername(username);
        return this.memberService.register(member);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Member view(@PathVariable("id") Long id) {
        return this.memberService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Member create(@RequestBody @Valid Member member) {
        return memberService.save(member);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Member update(@PathVariable("id") Long id, @RequestBody Member member) {
        member.setId(id);
        return memberService.save(member);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.memberService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.memberService.delete(id);
    }

}
