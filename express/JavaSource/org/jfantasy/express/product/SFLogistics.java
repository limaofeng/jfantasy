package org.jfantasy.express.product;

import com.fantasy.framework.httpclient.HttpClientUtil;
import com.fantasy.framework.httpclient.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class SFLogistics implements Logistics {

    private final static Log LOG = LogFactory.getLog(SFLogistics.class);

    @Override
    public String getId() {
        return "sf";
    }

    @Override
    public String getName() {
        return "顺丰速递";
    }

    @Override
    public Bill execute(String sn) {
        try {
            Response response = HttpClientUtil.doGet("http://www.sf-express.com/sf-service-web/service/bills/" + sn + "/routes?app=bill&lang=sc&region=cn&translate=");
            System.out.println(response.getStatusCode());
            System.out.println(response.getText());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

}
