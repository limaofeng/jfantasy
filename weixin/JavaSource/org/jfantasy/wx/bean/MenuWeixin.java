package org.jfantasy.wx.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import java.util.List;

/**
 * 微信通用接口凭证
 * Created by zzzhong on 2014/6/18.
 */
@Entity
@Table(name = "WX_MENU")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MenuWeixin extends BaseBusEntity {
    public MenuWeixin() {
    }

    public MenuWeixin(String name, String type, Integer layer, Integer sort, String url, String key, MenuWeixin parent) {
        this.name = name;
        this.type = type;
        this.layer = layer;
        this.sort = sort;
        this.url = url;
        this.key = key;
        this.parent = parent;
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    //菜单类型
    @Column(name = "\"TYPE\"")
    private String type;
    //菜单名称
    @Column(name = "\"NAME\"")
    private String name;
    //菜单key
    @Column(name = "\"KEY\"")
    private String key;
    //菜单的url
    @Column(name = "URL")
    private String url;
    //菜单级别 一级菜单或者二级菜单
    @Column(name = "LAYER")
    private Integer layer;
    //排序
    @Column(name = "SORT")
    private Integer sort;// 排序

    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "P_ID", foreignKey = @ForeignKey(name = "FK_MENU_PARENT"))
    @JsonManagedReference
    private MenuWeixin parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<MenuWeixin> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public MenuWeixin getParent() {
        return parent;
    }

    public void setParent(MenuWeixin parent) {
        this.parent = parent;
    }

    public List<MenuWeixin> getChildren() {
        return children;
    }

    public void setChildren(List<MenuWeixin> children) {
        this.children = children;
    }
}
