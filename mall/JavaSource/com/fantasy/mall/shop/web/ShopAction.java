package com.fantasy.mall.shop.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.shop.service.ShopService;

import org.springframework.beans.factory.annotation.Autowired;

public class ShopAction extends ActionSupport {

    @Autowired
    private ShopService shopService;

}
