package org.jfantasy.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.common.bean.converter.AreaTagsConverter;
import org.jfantasy.common.bean.enums.AreaTag;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

@ApiModel("地区信息")
@Entity
@Table(name = "SYS_AREA")
@JsonIgnoreProperties(value = {"hibernate_lazy_initializer", "handler", "creator", "create_time", "modifier", "modify_time", "parent", "children"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Area extends BaseBusEntity {

    private static final long serialVersionUID = -2158109459123036967L;

    public static final String PATH_SEPARATOR = ",";// 路径分隔符

    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 50)
    private String id;
    @ApiModelProperty("名称")
    @Column(name = "NAME", nullable = false)
    private String name;// 名称
    @ApiModelProperty("完整地区名称")
    @Column(name = "DISPLAY_NAME", nullable = false, length = 3000)
    private String displayName;// 完整地区名称
    @ApiModelProperty(hidden = true)
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;// 路径
    @ApiModelProperty("标签")
    @Column(name = "TAGS", nullable = false, length = 30)
    @Convert(converter = AreaTagsConverter.class)
    private AreaTag[] tags;
    @ApiModelProperty("层级")
    @Column(name = "LAYER", nullable = false)
    private Integer layer;// 层级
    @ApiModelProperty("排序")
    @Column(name = "SORT")
    private Integer sort;// 排序
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(hidden = true)
    @JoinColumn(name = "P_ID", foreignKey = @ForeignKey(name = "FK_AREA_PARENT"))
    private Area parent;// 上级地区
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort asc")
    private List<Area> children;// 下级地区

    public Area() {
    }

    private Area(String id) {
        this();
        this.id = id;
    }

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

    public AreaTag[] getTags() {
        return tags;
    }

    public void setTags(AreaTag[] tags) {
        this.tags = tags;
    }

    @Transient
    public void setParentId(String id) {
        this.setParent(new Area(id));
    }

    @Transient
    public String getParentId() {
        return this.parent != null ? this.parent.getId() : null;
    }

}
