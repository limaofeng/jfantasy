package org.jfantasy.mall.goods.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.member.bean.Member;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.Date;

/**
 * 到货通知
 * 
 * @author 李茂峰
 * @since 2013-9-21 下午5:47:17
 * @version 1.0
 */
@Entity
@Table(name = "MALL_GOODS_NOTIFY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GoodsNotify extends BaseBusEntity {

	private static final long serialVersionUID = 2814795887062166006L;

	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	private Long id;
	@Column(name="EMAIL",nullable = false, updatable = false)
	private String email;// 通知E-mail
	@Column(name="SEND_DATE")
	private Date sendDate;// 发送时间
	@Column(name="SENT",nullable = false)
	private boolean sent;// 是否已发送
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PRODUCT_ID",nullable = false, updatable = false,foreignKey = @ForeignKey(name = "FK_GOODS_NOTIFY_PRODUCT"))

	private Product product;// 货品
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MEMBER_ID",updatable = false,foreignKey = @ForeignKey(name = "FK_GOODS_NOTIFY_MEMBER"))
	private Member member;// 会员

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getSendDate() {
        if (this.sendDate == null) {
            return null;
        }
        return (Date) this.sendDate.clone();
	}

	public void setSendDate(Date sendDate) {
        if (sendDate == null) {
            this.sendDate = null;
        } else {
            this.sendDate = (Date) sendDate.clone();
        }
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}