package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.PropertiesConverter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.pay.bean.converter.ProjectConverter;
import org.jfantasy.pay.bean.databind.ProjectDeserializer;
import org.jfantasy.pay.bean.enums.TxChannel;
import org.jfantasy.pay.bean.enums.TxStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 交易表
 */
@Entity
@Table(name = "PAY_TRANSACTION")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "payments", "refunds", "unionId"})
public class Transaction extends BaseBusEntity {

    private static final long serialVersionUID = 3296031463173407900L;

    public final static String STAGE = "stage";
    public final static String STAGE_PAYMENT = "stage_payment";
    public final static String STAGE_REFUND = "stage_refund";
    public final static String ORDER_KEY = "order_key";
    public static final String ORDER_SUBJECT = "order_subject";

    public final static String CARD_ID = "card_id";
    public static final String CARD_SUBJECT = "card_subject";

    /**
     * 交易流水号
     */
    @Id
    @Column(name = "SN", updatable = false)
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@org.hibernate.annotations.Parameter(name = "expression", value = "#DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('TX-SN' + #DateUtil.format('yyyyMMdd')), 5)")})
    private String sn;
    /**
     * 交易表唯一
     * 主要为了防止重复交易的发生
     * 格式为:projectKey|key
     */
    @Column(name = "UNION_ID", updatable = false, unique = true)
    private String unionId;
    /**
     * 转出账号<br/> 充值时,可以为空
     */
    @Column(name = "FROM_ACCOUNT")
    private String from;
    /**
     * 转入账号<br/>
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
    @Column(name = "CHANNEL")
    @Enumerated(EnumType.STRING)
    private TxChannel channel;
    /**
     * 交易状态
     */
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxStatus status;
    /**
     * 状态文本
     */
    @Column(name = "STATUS_TEXT", nullable = false)
    private String statusText;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;
    /**
     * 交易项目(转账/提现等)
     */
    @Column(name = "PROJECT", length = 500, updatable = false)
    @Convert(converter = ProjectConverter.class)
    @JsonDeserialize(using = ProjectDeserializer.class)
    private Project project;
    /**
     * 扩展字段,用于存储不同项目的关联信息
     */
    :
    @Convert(converter = PropertiesConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Properties properties;
    /** 支付记录 **/
    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Payment> payments = new ArrayList<Payment>();
    /** 退款记录 **/
    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Refund> refunds = new ArrayList<>();

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

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
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

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public static String generateUnionid(String projectKey, String orderKey) {
        return projectKey + ">" + orderKey;
    }

}
