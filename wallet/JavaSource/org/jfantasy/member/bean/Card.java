package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.member.bean.converter.CardStyleConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;

@Entity(name = "mem.Card")
@Table(name = "MEM_CARD")
@TableGenerator(name = "card_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_card:id", valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "wallet"})
public class Card extends BaseBusEntity {

    private static final long serialVersionUID = -1463056313189743496L;
    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "card_gen")
    private Long id;
    /**
     * 卡号
     */
    @JsonProperty("no")
    @Column(name = "CARD_NUMBER", length = 20)
    private String cardNo;
    /**
     * 钱包
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_CARD_WALLET"))
    private Wallet wallet;
    /**
     * 卡片样式
     */
    @Column(name = "CARD_STYLE", length = 1000)
    @Convert(converter = CardStyleConverter.class)
    private CardStyle cardStyle;
    /**
     * 卡金额
     */
    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    /**
     * 附加服务
     */
    @Column(name = "EXTRAS", length = 2000)
    @Convert(converter = MapConverter.class)
    private Properties extras;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public CardStyle getCardStyle() {
        return cardStyle;
    }

    public void setCardStyle(CardStyle cardStyle) {
        this.cardStyle = cardStyle;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Properties getExtras() {
        return extras;
    }

    public void setExtras(Properties extras) {
        this.extras = extras;
    }

    @Transient
    @JsonProperty("bind_time")
    public Date getBindTime() {
        return this.getCreateTime();
    }
}
