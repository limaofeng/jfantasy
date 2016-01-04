/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.config.WebAppSecurityInitializer.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年8月15日 下午4:38:09 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年8月15日    |    Administrator     |     Created 
 */
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


