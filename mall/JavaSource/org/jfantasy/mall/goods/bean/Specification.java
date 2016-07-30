package org.jfantasy.mall.goods.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.List;

/**
 * 商品规格
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-4-4 下午6:32:04
 */
@Entity
@Table(name = "MALL_SPECIFICATION")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties(value = {"hibernate_lazy_initializer", "handler", "handler"})
public class Specification extends BaseBusEntity {

    private static final long serialVersionUID = -2508802352043544093L;

    // 商品规格类型（文字类型、图片类型）
    public enum Type {
        text, picture
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 20)
    private String name;
    /**
     * 商品规格类型
     */
    @Column(name = "TYPE")
    private Type type;
    /**
     * 排序
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 商品规格值存储
     */
    @Column(name = "VALUE_STORE", length = 3000)
    private String valueStore;
    /**
     * 备注
     */
    @Column(name = "REMARK", length = 500)
    private String remark;
    /**
     * 使用该规格的商品
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MALL_GOODS_SPECIFICATION", joinColumns = @JoinColumn(name = "SPECIFICATION_ID"), inverseJoinColumns = @JoinColumn(name = "GOODS_ID"), foreignKey = @ForeignKey(name = "FK_SPECIFICATION_GOODS"))
    private List<Goods> goods;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValueStore() {
        return valueStore;
    }

    public void setValueStore(String valueStore) {
        this.valueStore = valueStore;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

}
