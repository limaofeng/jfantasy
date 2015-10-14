package com.fantasy.website.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SWP_DATA_ANALYZER")
public class DataAnalyzer extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 显示名称
     */
    @Column(name = "NAME", length = 50)
    private String name;

    /**
     * class 的 name
     */
    @Column(name = "CLASS_NAME", length = 1000)
    private String className;
    /**
     * 参数
     */
    @Column(name = "PARAMETER_STORE", length = 2000)
    private String parameterStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParameterStore() {
        return parameterStore;
    }

    public void setParameterStore(String parameterStore) {
        this.parameterStore = parameterStore;
    }
}
