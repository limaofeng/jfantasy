package com.fantasy.security.rest;

import com.fantasy.security.bean.Menu;
import com.fantasy.security.service.MenuService;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security/menus")
public class MenuController {

    @Autowired
    private transient MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> index() {
        return this.menuService.list(new Criterion[]{}, "sort", "asc");
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable("id") Long id) {
        this.menuService.delete(id);
    }

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Menu create(@RequestBody Menu menu) {
        return menuService.save(menu);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public Menu update(@PathVariable("id") Long id, @RequestBody Menu menu) {
        menu.setId(id);
        return menuService.save(menu);
    }

}
