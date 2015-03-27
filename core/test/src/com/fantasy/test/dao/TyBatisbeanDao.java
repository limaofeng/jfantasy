package com.fantasy.test.dao;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.test.bean.TyBatisbean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TyBatisbeanDao extends SqlMapper {

    /**
     * 根据Key查询数据
     *
     * @param key key
     * @return testbean对象
     */
    public TyBatisbean findUniqueByKey(String key);

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<TyBatisbean> selectAll();

    /**
     * 多参数查询方法测试
     * @param key p1
     * @param value p2
     * @return List<TyBatisbean>
     */
    public List<TyBatisbean> selectMultiParameters(@Param("key") String key,@Param("value") String value);

    /**
     * 插入数据
     *
     * @param testbean
     * @return 影响行数
     */
    public int insert(TyBatisbean testbean);

    /**
     * 更新数据
     *
     * @param testbean
     * @return 影响行数
     */
    public int update(TyBatisbean testbean);

    public int delete(String key);

    /**
     * 分页查询 用户基本信息表 的所有数据
     *
     * @param pager       翻页对象
     * @param tyBatisbean 查询对象
     * @return Pager<TyBatisbean>
     */
    public Pager<TyBatisbean> findPager(Pager<TyBatisbean> pager, TyBatisbean tyBatisbean);

    Pager<TyBatisbean> findSimplePager(Pager<TyBatisbean> pager, String value);

}
