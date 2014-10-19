package com.fantasy.mall.cart.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.mall.goods.bean.Product;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 购物车中的购物项
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 上午10:01:55
 */
@Entity
@Table(name = "MALL_CART_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "cart"})
public class CartItem extends BaseBusEntity {

    private static final long serialVersionUID = 8008780042070523210L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 数量
     */
    @Column(name = "QUANTITY", length = 10)
    private Integer quantity;
    /**
     * 价格
     */
    @Column(name = "PRICE", length = 10, scale = 2)
    private BigDecimal price;
    /**
     * 商品
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PRODUCT_ID",foreignKey = @ForeignKey(name = "FK_CART_ITEM_PRODUCT"))
    private Product product;
    /**
     * 所属购物车
     */
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "CART_ID",foreignKey =  @ForeignKey(name = "FK_ITEM_CART"))

    private Cart cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.setPrice(this.product.getPrice());
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * 获取优惠价格,若无优惠则返回原价格
     *
     * @return {BigDecimal}
     */
    @Transient
    public BigDecimal getPreferentialPrice() {
        BigDecimal price = this.getPrice();
        if (price == null) {
            price = this.getProduct().getPrice();
        }
        return price;
    }

    /**
     * 获取小计价格
     *
     * @return {BigDecimal}
     */
    @Transient
    public BigDecimal getSubtotalPrice() {
        return getPreferentialPrice().multiply(new BigDecimal(quantity));
    }

}
