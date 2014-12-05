package com.fantasy.framework.dao.mybatis.keygen.dao;

import com.fantasy.framework.dao.mybatis.keygen.bean.Sequence;
import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.stereotype.Repository;

/**
 *  MyBatis SequenceDao 接口
 */
@Repository
public interface SequenceDao extends SqlMapper {

    /**
     * 查询序列
     *
     * @param keyName keyName
     * @return Sequence
     */
    public Sequence findUniqueByKey(String keyName);

    /**
     * 创建序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
    public int insert(Sequence sequence);

    /**
     * 更新序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
    public int update(Sequence sequence);

}