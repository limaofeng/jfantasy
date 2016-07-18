package org.jfantasy.member.bean;

import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.member.bean.enums.InvoiceStatus;
import org.jfantasy.member.bean.enums.InvoiceType;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 发票管理
 */
public class Invoice extends BaseBusEntity {

    @ApiModelProperty("编号")
    @Id
    @Column(name = "NO", nullable = false, length = 8)
    private String no;
    @ApiModelProperty("类型")
    @Column(name = "TYPE", length = 10)
    private InvoiceType type;
    @ApiModelProperty("状态")
    @Column(name = "STATUS", length = 10)
    private InvoiceStatus status;
    @ApiModelProperty("内容")
    @Column(name = "content", length = 200)
    private String content;
    @ApiModelProperty("抬头")
    @Column(name = "TITLE", length = 200)
    private String title;
    @ApiModelProperty("金额")
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    /*************************************/
    /*          物流信息                  */
    /*************************************/
    @ApiModelProperty("物流公司")
    @Column(name = "LOGISTICS", length = 20)
    private String logistics;
    @ApiModelProperty("快递编号")
    @Column(name = "SHIP_NO", length = 20)
    private String shipNo;
    /*************************************/
    /*          收件人信息                */
    /*************************************/
    @ApiModelProperty("收货人姓名")
    @Column(name = "SHIP_NAME", nullable = false)
    private String shipName;
    @ApiModelProperty("收件人联系电话")
    @Column(name = "SHIP_TEL", nullable = false)
    private String shipTel;
    @ApiModelProperty("收件人地区编码")
    @Column(name = "SHIP_AREA_STORE", nullable = false)
    private String area;
    @ApiModelProperty("收件人地址")
    @Column(name = "SHIP_ADDRESS", nullable = false)
    private String shipAddress;// 收货地址
    @ApiModelProperty("手机人邮编")
    @Column(name = "SHIP_ZIP_CODE", nullable = false)
    private String shipZipCode;// 收货邮编

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public InvoiceType getType() {
        return type;
    }

    public void setType(InvoiceType type) {
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
}
