package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.pay.bean.converter.ProjectConverter;
import org.jfantasy.pay.bean.enums.TxChannel;
import org.jfantasy.pay.bean.enums.TxStatus;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * 交易表
 */
@Entity
@Table(name = "TRANSACTION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction extends BaseBusEntity {
    /**
     * 交易流水号
     */
    @Id
    @Column(name = "SN", updatable = false)
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@org.hibernate.annotations.Parameter(name = "expression", value = "'TX' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('TX-SN' + #DateUtil.format('yyyyMMdd')), 5)")})
    private String sn;
    /**
     * 转出账号<br/>
     * 线下渠道时 填写 线下转账账号<br/>
     * 第三方交易是 填写 PayConfigId
     */
    @Column(name = "FROM_ACCOUNT", nullable = false)
    private String from;
    /**
     * 转入账号<br/>
     * 线下渠道时 填写 线下转账账号<br/>
     * 第三方交易是 填写 PayConfigId
     */
    @Column(name = "TO_ACCOUNT", nullable = false)
    private String to;
    /**
     * 金额
     */
    @Column(name = "AMOUNT", nullable = false, updatable = false)
    private BigDecimal amount;
    /**
     * 交易渠道
     */
    @Column(name = "CHANNEL", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxChannel channel;
    /**
     * 交易状态
     */
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;
    /**
     * 交易项目(转账/提现等)
     */
    @Column(name = "PROJECT", length = 500)
    @Convert(converter = ProjectConverter.class)
    private Project project;
    /**
     * 扩展字段,用于存储不同项目的关联信息
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "PROPERTIES", columnDefinition = "MediumBlob")
    private Properties properties;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TxChannel getChannel() {
        return channel;
    }

    public void setChannel(TxChannel channel) {
        this.channel = channel;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JsonAnyGetter
    public Properties getProperties() {
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(PayConfig.class, "properties")) {
            return null;
        }
        return properties;
    }

    @JsonAnySetter
    public void set(String key, String value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.setProperty(key, value);
    }

    @Transient
    public String get(String key) {
        if (this.properties == null) return null;
        return this.properties.getProperty(key);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
