package com.fantasy.system.rest;

import com.fantasy.security.bean.Menu;
import com.fantasy.security.service.MenuService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/websites")
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/{key}/menus", method = RequestMethod.GET)
    public List<Menu> menus(@PathVariable("key") String key) {
        Website website = this.websiteService.findUniqueByKey(key);
        Long rootMenuId = website.getRootMenu().getId();
        return menuService.list(new Criterion[]{Restrictions.like("path", rootMenuId + Menu.PATH_SEPARATOR, MatchMode.START), Restrictions.ne("id", rootMenuId)}, "sort", "asc");
    }

}
