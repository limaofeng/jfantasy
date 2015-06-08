package com.fantasy.test.web;

import com.fantasy.security.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    @ResponseBody
    public User view(@PathVariable("id") Long id, HttpServletRequest req) {
        User user = new User();
        user.setId(id);
        user.setUsername("zhang");
        /*
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("user/view");
        */
        return user;
    }
}
