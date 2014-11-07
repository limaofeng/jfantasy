package com.fantasy.test.dao;

import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.test.bean.TMyBatisbean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TMyBatisbeanDao extends SqlMapper{

    /**
     *  根据Key查询数据
     * @param key key
     * @return testbean对象
     */
    public TMyBatisbean findUniqueByKey(String key);

    /**
     * 查询所有数据
     * @return
     */
    public List<TMyBatisbean> selectAll();

    /**
     * 插入数据
     * @param testbean
     * @return 影响行数
     */
    public int insert(TMyBatisbean testbean);

    /**
     * 更新数据
     * @param testbean
     * @return 影响行数
     */
    public int update(TMyBatisbean testbean);

}
