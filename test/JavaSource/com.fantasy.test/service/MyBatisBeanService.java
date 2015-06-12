package com.fantasy.test.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.test.bean.MyBatisBean;
import com.fantasy.test.dao.MyBatisBeanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyBatisBeanService {

    @Autowired
    private MyBatisBeanDao myBatisBeanDao;

    public MyBatisBean findUniqueByKey(String key){
        return this.myBatisBeanDao.findUniqueByKey(key);
    }

    public int insert(MyBatisBean testbean){
        return this.myBatisBeanDao.insert(testbean);
    }

    public int update(MyBatisBean testbean){
        return this.myBatisBeanDao.update(testbean);
    }

    public int delete(String key){
        return this.myBatisBeanDao.delete(key);
    }

    public List<MyBatisBean> selectAll(){
        return this.myBatisBeanDao.selectAll();
    }

    public MyBatisBean get(String test) {
        return this.myBatisBeanDao.findUniqueByKey(test);
    }

    public Pager<MyBatisBean> findPager(Pager<MyBatisBean> pager, MyBatisBean myBatisBean) {
        return this.myBatisBeanDao.findPager(pager, myBatisBean);
    }

    public Pager<MyBatisBean> findSimplePager(Pager<MyBatisBean> pager, String value) {
        return this.myBatisBeanDao.findSimplePager(pager, value);
    }

    public List<MyBatisBean> selectMultiParameters(String key, String value) {
        return this.myBatisBeanDao.selectMultiParameters(key, value);
    }
}
