package org.jfantasy.common.rest;

import io.swagger.annotations.Api;
import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.web.bind.annotation.*;

@Api(value = "indexes", description = " 外部框架 - lucene ")
@RestController
@RequestMapping
public class IndexedController {

    @RequestMapping(value = "/indexes/{classname}/rebuild", method = RequestMethod.GET)
    @ResponseBody
    public void rebuild(@PathVariable("classname") String classname) {
        BuguIndex.getInstance().rebuild(ClassUtil.forName(classname));
    }

}
