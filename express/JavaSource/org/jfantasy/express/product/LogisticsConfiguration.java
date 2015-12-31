package org.jfantasy.express.product;

import com.fantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LogisticsConfiguration implements InitializingBean {

    /**
     * 所有支持的物流查询
     */
    private List<Logistics> logisticses = new ArrayList<Logistics>();

    @Override
    public void afterPropertiesSet() throws Exception {
        logisticses.add(new SFLogistics());
    }

    // 获取所有支付产品集合
    public List<Logistics> getLogisticses() {
        return this.logisticses;
    }

    public Logistics getLogistics(String id) {
        return ObjectUtil.find(this.logisticses, "id", id);
    }

}
