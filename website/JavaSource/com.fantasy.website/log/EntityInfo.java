package org.jfantasy.website.log;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.website.runtime.ExecutionEntity;

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
