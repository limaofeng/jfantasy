package org.jfantasy.pay.builder;

import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.OrderServiceBuilder;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class HttpOrderServiceBuilder implements OrderServiceBuilder {

    @Override
    public CallType getCallType() {
        return CallType.restful;
    }

    @Override
    public OrderService build(Properties props) {
        throw new RestException("暂不支持 Http 的方式调用 OrderService .");
    }

}
