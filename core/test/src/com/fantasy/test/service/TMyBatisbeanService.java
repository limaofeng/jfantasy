package com.fantasy.test.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.test.bean.TyBatisbean;
import com.fantasy.test.dao.TyBatisbeanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TMyBatisbeanService {

    @Autowired
    private TyBatisbeanDao tyBatisbeanDao;

    public TyBatisbean findUniqueByKey(String key){
        return this.tyBatisbeanDao.findUniqueByKey(key);
    }

    public int insert(TyBatisbean testbean){
        return this.tyBatisbeanDao.insert(testbean);
    }

    public int update(TyBatisbean testbean){
        return this.tyBatisbeanDao.update(testbean);
    }

    public int delete(String key){
        return this.tyBatisbeanDao.delete(key);
    }

    public List<TyBatisbean> selectAll(){
        return this.tyBatisbeanDao.selectAll();
    }

    public TyBatisbean get(String test) {
        return this.tyBatisbeanDao.findUniqueByKey(test);
    }

    public Pager<TyBatisbean> findPager(Pager<TyBatisbean> pager, TyBatisbean tyBatisbean) {
        return this.tyBatisbeanDao.findPager(pager,tyBatisbean);
    }

    public Pager<TyBatisbean> findSimplePager(Pager<TyBatisbean> pager, String value) {
        return this.tyBatisbeanDao.findSimplePager(pager, value);
    }

    public List<TyBatisbean> selectMultiParameters(String key, String value) {
        return this.tyBatisbeanDao.selectMultiParameters(key, value);
    }
}
