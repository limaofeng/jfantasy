package org.jfantasy.framework.spring.mvc.http;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@Order(2)
@ControllerAdvice(basePackages = "org.jfantasy.*.rest")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super("callback", "jsonp");
    }

}