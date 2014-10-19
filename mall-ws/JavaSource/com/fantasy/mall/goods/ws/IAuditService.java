package com.fantasy.mall.goods.ws;

import com.fantasy.mall.goods.ws.dto.AuditPriceDTO;

public interface IAuditService {


    /**
     * 调价申请
     * auditPriceDTO
     */
    public void priceVerify(AuditPriceDTO auditPriceDTO);

    /**
     * 下线申请
     *
     * @param sn      商品编号
     * @param message 下线原因
     */
    public void downVerify(String sn, String message);


}
