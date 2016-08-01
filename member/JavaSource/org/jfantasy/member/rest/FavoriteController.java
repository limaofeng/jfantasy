package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.Favorite;
import org.jfantasy.member.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "favorites", description = "收藏夹")
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Favorite.class, name = {"member"}))
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Favorite save(@Validated(RESTful.POST.class) @RequestBody Favorite favorite) {
        return favoriteService.save(favorite);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        favoriteService.delete(id);
    }

}