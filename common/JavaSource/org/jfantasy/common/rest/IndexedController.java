package org.jfantasy.common.rest;

import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class IndexedController {

    @RequestMapping(value = "/indexes/{classname}/rebuild", method = RequestMethod.GET)
    @ResponseBody
    public void rebuild(@PathVariable("classname") String classname) {
        BuguIndex.getInstance().rebuild(ClassUtil.forName(classname));
    }

}
