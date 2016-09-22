package org.jfantasy.pay;


import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.enums.CallType;

import java.util.Map;

public interface OrderServiceBuilder<T extends OrderService> {

    CallType getCallType();

    T build(Map props);

}
