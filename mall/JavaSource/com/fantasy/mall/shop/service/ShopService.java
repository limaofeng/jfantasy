package com.fantasy.mall.shop.service;

import com.fantasy.mall.shop.dao.ShopDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class ShopService {

    @Autowired
    private ShopDao shopDao;

}
