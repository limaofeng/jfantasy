package org.test;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.junit.Test;

import javax.xml.namespace.QName;

/**
 * Created by lmf on 14-9-17.
 */
public class AxisTest {

    @Test
    public void test() throws AxisFault {
        //http://180.153.176.78:8082/bth/services/GoodsService?wsdl
        RPCServiceClient serviceClient = new RPCServiceClient();
        Options options = serviceClient.getOptions();
        options.setTo(new EndpointReference("http://180.153.176.78:8082/bth/services".concat("/").concat("GoodsService")));
        QName opQName = new QName("http://ws.goods.mall.fantasy.com", "getGoodsById");
        Object[] response = serviceClient.invokeBlocking(opQName, new Object[]{33L}, new Class[]{GoodsDTO.class});
        GoodsDTO o = response.length > 0 ? (GoodsDTO) response[0] : null;
        System.out.println(o.getName());
    }

}
