package org.jfantasy.member.rest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.enums.InvoiceStatus;

import javax.validation.constraints.NotNull;

public class InvoiceForm {
    @ApiModelProperty("物流公司")
    private String logistics;
    @ApiModelProperty("快递编号")
    @JsonProperty("ship_no")
    private String shipNo;
    @NotNull(groups = {RESTful.PATCH.class})
    @ApiModelProperty("状态")
    private InvoiceStatus status;

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

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}
