package org.jfantasy.common.rest;

import io.swagger.annotations.Api;
import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "indexes", description = " 外部框架 - lucene ")
@RestController
@RequestMapping
public class IndexedController {

    @Autowired
    private BuguIndex buguIndex;

    @RequestMapping(value = "/indexes/{classname}/rebuild", method = RequestMethod.GET)
    @ResponseBody
    public void rebuild(@PathVariable("classname") String classname) {
        buguIndex.rebuild(ClassUtil.forName(classname));
    }

}
