package com.fantasy.wx.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 微信通用接口凭证
 * Created by zzzhong on 2014/6/18.
 */
@Entity(name="WxMenu")
@Table(name = "WX_MENU")
public class Menu extends BaseBusEntity {
    public Menu() {
    }

    public Menu(String name, String type, Integer layer, Integer sort, String url, String key, Menu parent) {
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
    private Menu parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<Menu> children;

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

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
