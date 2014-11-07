package com.fantasy.test.service;

import com.fantasy.test.bean.TMyBatisbean;
import com.fantasy.test.dao.TMyBatisbeanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TMyBatisbeanService {

    @Autowired
    private TMyBatisbeanDao testDao;

    public TMyBatisbean findUniqueByKey(String key){
        return this.testDao.findUniqueByKey(key);
    }

    public int insert(TMyBatisbean testbean){
        int count = this.testDao.insert(testbean);
        return count;
    }

    public int update(TMyBatisbean testbean){
        int count = this.testDao.update(testbean);
        return count;
    }

    public List<TMyBatisbean> selectAll(){
        List<TMyBatisbean> testbeanList = this.testDao.selectAll();
        return testbeanList;
    }
}
