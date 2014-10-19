package com.fantasy.mall.shop.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.shop.service.ShopService;

import javax.annotation.Resource;

public class ShopAction extends ActionSupport {

    @Resource
    private ShopService shopService;

}
