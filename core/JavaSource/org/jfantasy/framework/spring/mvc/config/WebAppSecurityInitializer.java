package org.jfantasy.framework.spring.mvc.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 *Description: <类功能描述>. <br>
 *<p>
	<使用说明>
 </p>
 *Makedate:2014年8月15日 下午4:38:09
 * @author Administrator
 * @version V1.0
 */
@Order(2)
public class WebAppSecurityInitializer  extends AbstractSecurityWebApplicationInitializer
{
	//servletContext.addListener("org.springframework.security.web.session.HttpSessionEventPublisher");
	//session监听器
	@Override
	protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}


