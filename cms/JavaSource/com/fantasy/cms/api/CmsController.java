package com.fantasy.cms.api;

import com.fantasy.security.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CmsController {

    @RequestMapping("/api/{username}")
    @ResponseBody
    public User api(@PathVariable String username) {
        System.out.println("=====hello"+username);
        return new User();
    }

}
