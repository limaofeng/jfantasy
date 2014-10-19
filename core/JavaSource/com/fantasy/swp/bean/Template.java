package com.fantasy.swp.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 模板
 * <p/>
 * 用于生成静态页面时的thml模板配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 上午9:21:05
 */
@Entity
@Table(name = "SWP_TEMPLATE")
public class Template extends BaseBusEntity {

    private static final long serialVersionUID = 6516821318292532274L;

    public enum Type {
        velocity("velocity"), freeMarker("freeMarker");
        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 模板名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 模板类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private Type type;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 模板文件存放的位置
     */
    @Column(name = "FILEPATH")
    private String filePath;
    /**
     * 数据接口定义
     */
    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY)
    private List<DataInferface> dataInferfaces;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<DataInferface> getDataInferfaces() {
        return dataInferfaces;
    }

    public void setDataInferfaces(List<DataInferface> dataInferfaces) {
        this.dataInferfaces = dataInferfaces;
    }
}
