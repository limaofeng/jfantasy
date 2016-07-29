package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.enums.InvoiceStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;

/**
 * 发票管理
 */
@Entity
@Table(name = "MEM_INVOICE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Invoice extends BaseBusEntity {

    @Null(groups = {RESTful.POST.class})
    @Id
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    @Column(name = "ID", updatable = false)
    private Long id;
    @ApiModelProperty("编号")
    @GeneratedValue(generator = "serialnumber")
    @GenericGenerator(
            name = "serialnumber",
            strategy = "serialnumber",
            parameters = {
                    @Parameter(
                            name = "expression",
                            value = "#DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('INVOICE-NO' + #DateUtil.format('yyyyMMdd')), 5)"
                    )})
    @Column(name = "NO", nullable = false, length = 20)
    private String no;
    @ApiModelProperty("类型")
    @Column(name = "TYPE", length = 10)
    private String type;
    @Null(groups = {RESTful.POST.class})
    @ApiModelProperty("状态")
    @Column(name = "STATUS", length = 10)
    private InvoiceStatus status;
    /*************************************/
    /*          发票内容                  */
    /*************************************/
    @NotNull(groups = {RESTful.POST.class})
    @ApiModelProperty("内容")
    @Column(name = "content", length = 200)
    private String content;
    @NotNull(groups = {RESTful.POST.class})
    @ApiModelProperty("抬头")
    @Column(name = "TITLE", length = 200)
    private String title;
    @Null(groups = {RESTful.POST.class})
    @ApiModelProperty("金额")
    @Column(name = "AMOUNT", scale = 2, nullable = false)
    private BigDecimal amount;
    /*************************************/
    /*          物流信息                  */
    /*************************************/
    @ApiModelProperty("物流公司")
    @Column(name = "LOGISTICS", length = 20)
    private String logistics;
    @JsonProperty("ship_no")
    @ApiModelProperty("快递编号")
    @Column(name = "SHIP_NO", length = 20)
    private String shipNo;
    /*************************************/
    /*          收件人信息                */
    /*************************************/
    @NotNull(groups = {RESTful.POST.class})
    @JsonProperty("ship_name")
    @ApiModelProperty("收货人姓名")
    @Column(name = "SHIP_NAME", nullable = false)
    private String shipName;
    @NotNull(groups = {RESTful.POST.class})
    @JsonProperty("ship_tel")
    @ApiModelProperty("收件人联系电话")
    @Column(name = "SHIP_TEL", nullable = false)
    private String shipTel;
    @NotNull(groups = {RESTful.POST.class})
    @ApiModelProperty("收件人地区编码")
    @Column(name = "SHIP_AREA_STORE", nullable = false)
    private String area;
    @NotNull(groups = {RESTful.POST.class})
    @ApiModelProperty("收件人地址")
    @JsonProperty("ship_address")
    @Column(name = "SHIP_ADDRESS", nullable = false)
    private String shipAddress;// 收货地址
    @NotNull(groups = {RESTful.POST.class})
    @ApiModelProperty("手机人邮编")
    @JsonProperty("ship_zip_code")
    @Column(name = "SHIP_ZIP_CODE", nullable = false)
    private String shipZipCode;// 收货邮编
    /*************************************/
    /*             开票方                */
    /*************************************/
    @Null(groups = {RESTful.POST.class})
    @Column(name = "TARGET_TYPE", length = 10)
    private String targetType;
    @Null(groups = {RESTful.POST.class})
    @Column(name = "TARGET_ID", length = 20)
    private String targetId;
    /*************************************/
    /*             开票项目               */
    /*************************************/
    @NotNull(groups = {RESTful.POST.class})
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<InvoiceItem> items;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_MEM_INVOICE_MEMBER"))
    private Member member;

    public Invoice() {
    }

    public Invoice(Long id) {
        this.setId(id);
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipTel() {
        return shipTel;
    }

    public void setShipTel(String shipTel) {
        this.shipTel = shipTel;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipZipCode() {
        return shipZipCode;
    }

    public void setShipZipCode(String shipZipCode) {
        this.shipZipCode = shipZipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
