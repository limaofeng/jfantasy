package com.fantasy.test.service;

import com.fantasy.test.bean.Testbean;
import com.fantasy.test.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hebo on 2014/9/18.
 */
@Repository
public class TestService {

    @Autowired
    private TestDao testDao;

    public Testbean findUniqueByKey(String key){
        return this.testDao.findUniqueByKey(key);
    }

    public int insert(Testbean testbean){
        int count = this.testDao.insert(testbean);
        return count;
    }

    public int update(Testbean testbean){
        int count = this.testDao.update(testbean);
        return count;
    }

    public List<Testbean> selectAll(){
        List<Testbean> testbeanList = this.testDao.selectAll();
        return testbeanList;
    }
}
