package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.CardBatchStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 会员卡发行批次
 */
@Entity
@Table(name = "PAY_CARD_BATCH")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CardBatch extends BaseBusEntity {

    @ApiModelProperty("批次号")
    @Id
    @Column(name = "NUMBER", length = 20)
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
    @JoinColumn(name = "CARD_TYPE", nullable = false)
    private CardType cardType;
    /**
     * 卡设计
     */
    @JsonProperty("card_design")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_DESIGN", nullable = false)
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
}
