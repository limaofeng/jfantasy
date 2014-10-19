package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.mall.goods.ws.IAuditService;
import com.fantasy.mall.goods.ws.dto.AuditPriceDTO;

/**
 * Created by yhx on 2014/7/15.
 */
public class AuditService extends WebServiceClient implements IAuditService {

    public AuditService(){
        super("AuditService");
    }


    @Override
    public void priceVerify(AuditPriceDTO auditPriceDTO) {
        super.invokeOption("priceVerify", new Object[]{auditPriceDTO},new Class[]{});
    }

    @Override
    public void downVerify(String sn, String message) {
        super.invokeOption("downVerify", new Object[]{sn,message},new Class[]{});
    }
}
