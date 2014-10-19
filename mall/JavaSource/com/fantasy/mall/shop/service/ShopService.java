package com.fantasy.mall.shop.service;

import com.fantasy.mall.shop.dao.ShopDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ShopService {

    @Resource
    private ShopDao shopDao;

}
