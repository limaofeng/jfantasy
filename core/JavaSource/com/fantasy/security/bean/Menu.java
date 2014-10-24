package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.enums.MenuType;
import org.apache.struts2.json.annotations.JSON;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "AUTH_MENU")
@JsonIgnoreProperties({"hibernateLazyInitializer", "parent"})
public class Menu extends BaseBusEntity {

    private static final long serialVersionUID = -3361634609328758218L;

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 菜单名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 树路径
     */
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;
    /**
     * 菜单值
     */
    @Column(name = "VALUE", length = 200)
    private String value;
    /**
     * 菜单类型
     */
    @Column(name = "TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private MenuType type;
    /**
     * 菜单对应的图标
     */
    @Column(name = "ICON", length = 50)
    private String icon;

    /**
     * 菜单描述
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 下级菜单
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    private List<Menu> children;
    /**
     * 上级菜单
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PID",foreignKey = @ForeignKey(name="FK_AUTH_MENU_PID"))

    private Menu parent;
    /**
     * 菜单是否选中
     */
    @Transient
    private boolean selected;

    public Menu() {
    }

    public Menu(Long parentId) {
        this.parent = new Menu();
        this.parent.id = parentId;
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

    @JSON(serialize = false)
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if ((ObjectUtil.isNotNull(this.parent)) && (selected))
            this.parent.setSelected(selected);
        else if ((ObjectUtil.isNotNull(getChildren())) && (!selected))
            for (Menu menu : getChildren())
                menu.setSelected(selected);
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @JsonProperty("parentId")
    @JsonSerialize(using = MenuParentSerialize.class)
    public Menu getParent() {
        return this.parent;
    }

    @JsonIgnore
    @JSON(serialize = false)
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}