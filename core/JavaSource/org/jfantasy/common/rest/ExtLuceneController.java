package org.jfantasy.common.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "common-exts-lucene", description = " 外部框架 - lucene ")
@RestController
@RequestMapping("/common/exts/lucene")
public class ExtLuceneController {

    @RequestMapping(value = "/indexs", method = RequestMethod.GET)
    @ResponseBody
    public void indexs() {

    }

}
