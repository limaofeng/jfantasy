package com.fantasy.framework.ws.axis2.handlers;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogHandler extends AbstractHandler implements Handler {
	private static final Log log = LogFactory.getLog(LogHandler.class);
	private String name;

	public String getName() {
		return this.name;
	}

	public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
		HttpServletRequest request = (HttpServletRequest) msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		HttpServletResponse response = (HttpServletResponse) msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE);
		System.out.println("******************************************");
		System.out.println("FLOW:"+msgContext.getFLOW());
		System.out.println("response:" + response);
		System.out.println("request:" + request);
		System.out.println("REMOTE_ADDR:" + msgContext.getProperty("REMOTE_ADDR"));
		System.out.println("MessageID:" + msgContext.getMessageID());
		System.out.println("LogIDString:" + msgContext.getLogCorrelationID());
		System.out.println("******************************************");
		if(msgContext.getFLOW() == 2){
			System.out.println(msgContext);
		}

		SOAPEnvelope envelope = msgContext.getEnvelope();
		try {
			if (request == null)
				System.out.println(envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InvocationResponse.CONTINUE;
	}

	public void revoke(MessageContext msgContext) {
		log.info(msgContext.getEnvelope().toString());
	}

	public void setName(String name) {
		this.name = name;
	}
}