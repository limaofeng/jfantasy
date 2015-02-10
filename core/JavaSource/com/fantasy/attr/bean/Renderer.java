package com.fantasy.attr.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 字段渲染器
 */
@Entity
@Table(name = "ATTR_RENDERER")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Renderer extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 新增模板
     */
    @Transient
    private String insertTemplate;
    /**
     * 编辑模板
     */
    @Transient
    private String updateTemplate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
