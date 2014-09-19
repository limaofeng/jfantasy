package com.fantasy.test.dao;

import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.test.bean.Testbean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hebo on 2014/9/18.
 */
@Repository
public interface TestDao extends SqlMapper{

    /**
     *  根据Key查询数据
     * @param key key
     * @return testbean对象
     */
    public Testbean findUniqueByKey(String key);

    /**
     * 查询所有数据
     * @return
     */
    public List<Testbean> selectAll();

    /**
     * 插入数据
     * @param testbean
     * @return 影响行数
     */
    public int insert(Testbean testbean);

    /**
     * 更新数据
     * @param testbean
     * @return 影响行数
     */
    public int update(Testbean testbean);

}
