package com.fantasy.common.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "common-exts", description = " 外部框架 ")
@RestController
@RequestMapping("/common/exts")
public class ExtController {

    @RequestMapping(value = "/lucene", method = RequestMethod.GET)
    @ResponseBody
    public void lucene() {
    }

}
