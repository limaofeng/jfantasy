package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.security.bean.enums.MenuType;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@ApiModel(value = "菜单")
@Entity
@Table(name = "AUTH_MENU")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parent"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends BaseBusEntity {

    private static final long serialVersionUID = -3361634609328758218L;

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    @ApiModelProperty("ID")
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 菜单名称
     */
    @ApiModelProperty("名称")
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 树路径
     */
    @ApiModelProperty("路径")
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;
    /**
     * 菜单值
     */
    @ApiModelProperty("值")
    @Column(name = "VALUE", length = 200)
    private String value;
    /**
     * 菜单类型
     */
    @ApiModelProperty("类型")
    @Column(name = "TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private MenuType type;
    /**
     * 菜单对应的图标
     */
    @ApiModelProperty("图标")
    @Column(name = "ICON", length = 50)
    private String icon;
    /**
     * 菜单描述
     */
    @ApiModelProperty("描述")
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 层级
     */
    @ApiModelProperty("层级")
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 下级菜单
     */
    @ApiModelProperty(hidden = true)
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<Menu> children;
    /**
     * 上级菜单
     */
    @ApiModelProperty("上级菜单")
    @JsonProperty("parentId")
    @JsonSerialize(using = MenuParentSerialize.class)
    @JsonDeserialize(using = MenuParentDeserialize.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_AUTH_MENU_PID"))
    @JsonManagedReference
    private Menu parent;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public MenuType getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return this.sort;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Menu getParent() {
        return this.parent;
    }

    @JsonIgnore
    public GrantedAuthority getMenuAuthoritie() {
        return new SimpleGrantedAuthority("MENU_" + getId());
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static class MenuParentSerialize extends JsonSerializer<Menu> {
        @Override
        public void serialize(Menu menu, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(menu.getId() != null ? menu.getId().toString() : "");
        }
    }

    public static class MenuParentDeserialize extends JsonDeserializer<Menu> {
        @Override
        public Menu deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new Menu(jp.getValueAsLong());
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}