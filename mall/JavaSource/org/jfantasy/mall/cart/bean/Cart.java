package org.jfantasy.mall.cart.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import java.util.List;

/**
 * 购物车
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 上午09:48:09
 */
@Entity
@Table(name = "MALL_CART")
@JsonIgnoreProperties({"hibernate_lazy_initializer"})
public class Cart extends BaseBusEntity {

    private static final long serialVersionUID = -843957670346812795L;

    public enum OwnerType {
        /**
         * 会员
         */
        Member,
        /**
         * cookie
         */
        Cookie
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 购物车对于的用户信息
     */
    @Column(name = "OWNER", length = 50, unique = true)
    private String owner;

    /**
     * 购物车所有者类型
     */
    private OwnerType ownerType;

    @JsonBackReference
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @OrderBy("id ASC")
    private List<CartItem> cartItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

}
