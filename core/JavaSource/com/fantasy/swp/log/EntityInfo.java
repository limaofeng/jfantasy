package com.fantasy.swp.log;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.swp.runtime.ExecutionEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * 数据引用表
 */
public class EntityInfo extends BaseBusEntity {
    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    private Long id;
    /**
     * 数据类型
     */
    private String className;
    /**
     * 目标Id
     */
    private String targetId;
    /**
     * 引用到该条数据的实例
     */
    private List<ExecutionEntity> executionEntity;
}
