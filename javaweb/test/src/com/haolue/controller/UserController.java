package com.haolue.controller;

import com.fantasy.security.bean.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @RequestMapping("/api/{username}")
    @ResponseBody
    public User api(@PathVariable String username) {
        System.out.println("=====hello"+username);
        return new User();
    }

}