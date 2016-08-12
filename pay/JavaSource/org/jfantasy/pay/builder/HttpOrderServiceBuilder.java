package org.jfantasy.pay.builder;

import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.OrderServiceBuilder;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.CallType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Component
public class HttpOrderServiceBuilder implements OrderServiceBuilder {

    @Override
    public CallType getCallType() {
        return CallType.restful;
    }

    @Override
    public OrderService build(Properties props) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("",Object.class);
        throw new RestException("暂不支持 Http 的方式调用 OrderService .");
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("http://114.55.142.155:8000/orders/123456",Object.class);
    }

}
