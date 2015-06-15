package com.fantasy.framework.dao.mybatis.keygen.dao;

import com.fantasy.framework.dao.mybatis.keygen.bean.Sequence;
import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * MyBatis SequenceDao 接口
 */
@Repository
public interface SequenceDao extends SqlMapper {

    /**
     * 查询序列
     *
     * @param keyName keyName
     * @return Sequence
     */
    Sequence findUniqueByKey(String keyName);

    /**
     * 创建序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
    int insert(Sequence sequence);

    /**
     * 更新序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
    int update(Sequence sequence);

    /**
     * 获取表中数据的max(id)
     *
     * @param table 表明
     * @param key   字段
     * @return max id
     */
    Integer queryTableSequence(@Param("table") String table, @Param("key") String key);
}