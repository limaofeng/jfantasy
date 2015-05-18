package com.fantasy.test.dao;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.test.bean.MyBatisBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyBatisBeanDao extends SqlMapper {

    /**
     * 根据Key查询数据
     *
     * @param key key
     * @return testbean对象
     */
    MyBatisBean findUniqueByKey(String key);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<MyBatisBean> selectAll();

    /**
     * 多参数查询方法测试
     *
     * @param key   p1
     * @param value p2
     * @return List<TyBatisbean>
     */
    List<MyBatisBean> selectMultiParameters(@Param("key") String key, @Param("value") String value);

    /**
     * 插入数据
     *
     * @param testbean
     * @return 影响行数
     */
    int insert(MyBatisBean testbean);

    /**
     * 更新数据
     *
     * @param testbean
     * @return 影响行数
     */
    int update(MyBatisBean testbean);

    int delete(String key);

    /**
     * 分页查询 用户基本信息表 的所有数据
     *
     * @param pager       翻页对象
     * @param myBatisBean 查询对象
     * @return Pager<TyBatisbean>
     */
    Pager<MyBatisBean> findPager(Pager<MyBatisBean> pager, MyBatisBean myBatisBean);

    Pager<MyBatisBean> findSimplePager(Pager<MyBatisBean> pager, String value);

}
