package com.fantasy.wx.menu.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 微信通用接口凭证
 * Created by zzzhong on 2014/6/18.
 */
@Entity
@Table(name = "WX_MENU")
public class WxMenu extends BaseBusEntity{
    public WxMenu(){}
    public WxMenu(String name, String type, Integer layer, Integer sort, String url, String key,WxMenu parent){
        this.name=name;
        this.type=type;
        this.layer=layer;
        this.sort=sort;
        this.url=url;
        this.key=key;
        this.parent=parent;
    }
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "\"TYPE\"")
    private String type;
    @Column(name = "\"NAME\"")
    private String name;
    @Column(name = "\"KEY\"")
    private String key;
    @Column(name = "URL")
    private String url;
    @Column(name = "LAYER")
    private Integer layer;

    @Column(name = "SORT")
    private Integer sort;// 排序

    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "P_ID", foreignKey = @ForeignKey(name = "FK_MENU_PARENT"))
    @JsonManagedReference
    private WxMenu parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<WxMenu> children;

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

    public WxMenu getParent() {
        return parent;
    }

    public void setParent(WxMenu parent) {
        this.parent = parent;
    }

    public List<WxMenu> getChildren() {
        return children;
    }

    public void setChildren(List<WxMenu> children) {
        this.children = children;
    }
}
