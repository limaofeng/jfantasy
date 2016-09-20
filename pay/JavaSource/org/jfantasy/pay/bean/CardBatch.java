package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.spring.validation.Use;
import org.jfantasy.pay.bean.enums.CardBatchStatus;
import org.jfantasy.pay.validators.CardBatchNoCannotRepeatValidator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 会员卡发行批次
 */
@Entity
@Table(name = "PAY_CARD_BATCH")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class CardBatch extends BaseBusEntity {

    private static final long serialVersionUID = -188799483821082297L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 批次号
     */
    @Column(name = "NUMBER", length = 20, unique = true)
    @Use(vali = CardBatchNoCannotRepeatValidator.class, groups = {RESTful.POST.class})
    private String no;
    /**
     * 发行批次名称
     */
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;
    /**
     * 卡类型
     */
    @JsonProperty("card_type")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_TYPE", nullable = false, foreignKey = @ForeignKey(name = "FK_CARD_TYPE_DESIGN"))
    private CardType cardType;
    /**
     * 卡设计
     */
    @JsonProperty("card_design")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_DESIGN", nullable = false, foreignKey = @ForeignKey(name = "FK_CARD_BATCH_DESIGN"))
    private CardDesign cardDesign;
    /**
     * 会员卡批次状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private CardBatchStatus status;
    /**
     * 发行数量
     */
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
    /**
     * 发行时间
     */
    @JsonProperty("release_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASE_TIME")
    private Date releaseTime;
    /**
     * 发行说明
     */
    @JsonProperty("release_notes")
    @Column(name = "RELEASE_NOTES", length = 3000, nullable = false)
    private String releaseNotes;

    @Transient
    private List<Log> logs;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public CardBatchStatus getStatus() {
        return status;
    }

    public void setStatus(CardBatchStatus status) {
        this.status = status;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardDesign getCardDesign() {
        return cardDesign;
    }

    public void setCardDesign(CardDesign cardDesign) {
        this.cardDesign = cardDesign;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
