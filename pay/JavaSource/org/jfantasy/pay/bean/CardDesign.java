package org.jfantasy.pay.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.spring.validation.Use;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.pay.bean.converter.ExtraServiceConverter;
import org.jfantasy.pay.bean.converter.StylesConverter;
import org.jfantasy.pay.bean.enums.CardDesignStatus;
import org.jfantasy.pay.bean.enums.Usage;
import org.jfantasy.pay.validators.CardDesignKeyCannotRepeatValidator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PAY_CARD_DESIGN")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class CardDesign extends BaseBusEntity {

    @Id
    @Column(name = "CODE", nullable = false , updatable = false)
    @Use(vali = CardDesignKeyCannotRepeatValidator.class, groups = {RESTful.POST.class})
    private String key;
    /**
     * 会员卡名称
     */
    @JsonProperty("card_name")
    @Column(name = "CARD_NAME", length = 20)
    private String cardName;
    /**
     * 会员卡使用方式
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "_USAGE", length = 20, updatable = false)
    private Usage usage;
    /**
     * 会员卡类型
     */
    @JsonProperty("card_type")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_TYPE", updatable = false, foreignKey = @ForeignKey(name = "FK_CARD_DESIGN_TYPE"))
    private CardType cardType;
    /**
     * 会员卡设计状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private CardDesignStatus status;
    /**
     * 面额
     */
    @Column(name = "AMOUNT", nullable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    /**
     * 卡牌样式(包含封面、等数据)
     */
    @Column(name = "STYLES", length = 200)
    @Convert(converter = StylesConverter.class)
    private Styles styles;
    /**
     * 说明
     */
    @Column(name = "NOTES", length = 3000)
    private String notes;
    /**
     * 附加服务
     */
    @Column(name = "EXTRAS", length = 1000)
    @Convert(converter = ExtraServiceConverter.class)
    private ExtraService[] extras;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Styles getStyles() {
        return styles;
    }

    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ExtraService[] getExtras() {
        return extras;
    }

    public void setExtras(ExtraService[] extras) {
        this.extras = extras;
    }

    public CardDesignStatus getStatus() {
        return status;
    }

    public void setStatus(CardDesignStatus status) {
        this.status = status;
    }

    @Transient
    private List<Log> logs;

    @Transient
    @JsonProperty("publish_time")
    public Date getPublishTime() {
        if (this.logs == null) {
            return null;
        }
        Log log = ObjectUtil.last(this.logs, "action", CardDesignStatus.publish.name());
        if (log == null) {
            return null;
        }
        return log.getLogTime();
    }

    @Transient
    @JsonProperty("destroy_time")
    public Date getDestroyTime() {
        if (this.logs == null) {
            return null;
        }
        Log log = ObjectUtil.find(logs, "action", CardDesignStatus.destroyed.name());
        if (log == null) {
            return null;
        }
        return log.getLogTime();
    }

    @Transient
    public String getRule() {
        if (this.getCardType() == null) {
            return null;
        }
        return this.getCardType().getKey() + " " + this.getKey() + " XXXX XXXX";
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

}
