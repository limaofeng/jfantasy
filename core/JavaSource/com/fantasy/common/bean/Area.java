package com.fantasy.common.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AREA")
@JsonIgnoreProperties(value = {"JavassistLazyInitializer", "creator", "createTime", "modifier", "modifyTime", "parent", "children"})
public class Area extends BaseBusEntity {

    private static final long serialVersionUID = -2158109459123036967L;

    public static final String PATH_SEPARATOR = ",";// 路径分隔符
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, length = 50)
    private String id;
    @Column(name = "NAME", nullable = false)
    private String name;// 名称
    @Column(name = "DISPLAY_NAME", nullable = false, length = 3000)
    private String displayName;// 完整地区名称
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;// 路径
    @Column(name = "LAYER", nullable = false)
    private Integer layer;// 层级
    @Column(name = "SORT")
    private Integer sort;// 排序
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "P_ID",foreignKey =@ForeignKey(name = "FK_AREA_PARENT") )
    private Area parent;// 上级地区

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort asc")
    private List<Area> children;// 下级地区

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Area getParent() {
        return parent;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    public List<Area> getChildren() {
        return children;
    }

    public void setChildren(List<Area> children) {
        this.children = children;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
