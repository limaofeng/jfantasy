package com.fantasy.framework.dao.mybatis.keygen.dao;

import com.fantasy.framework.dao.mybatis.keygen.bean.Sequence;
import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  MyBatis SequenceDao 接口
 */
@Repository
public interface SequencesDao extends SqlMapper {


    /**
     * 查询所有数据
     *
     * @return
     */
    public List<Sequence> selectAll();

    /**
     * 查询序列
     *
     * @param keyName keyName
     * @return Sequence
     */
    public Sequence findUniqueByKey(String keyName);

    /**
     * 更新序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
    public int update(Sequence sequence);

}