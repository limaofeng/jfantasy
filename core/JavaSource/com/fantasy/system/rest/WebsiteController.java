package com.fantasy.system.rest;

import com.fantasy.security.bean.Menu;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/website")
public class WebsiteController {

    @RequestMapping(value = "/{xx}/pp", method = RequestMethod.GET)
    public List<Menu> menus() {
        return null;
    }

}
