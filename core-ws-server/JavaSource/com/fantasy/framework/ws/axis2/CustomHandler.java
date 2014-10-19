package com.fantasy.framework.ws.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.Handler;

public class CustomHandler implements Handler {
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void cleanup() {
		System.out.println("###########cleanup###########");
	}

	public void flowComplete(MessageContext ctx) {
		System.out.println("###########flowComplete###########");
		System.out.println(ctx.getEnvelope().toString());
	}

	public HandlerDescription getHandlerDesc() {
		System.out.println("###########getHandlerDesc###########");
		return null;
	}

	public Parameter getParameter(String name) {
		System.out.println("###########getParameter###########");
		return null;
	}

	public void init(HandlerDescription handlerDescription) {
		System.out.println("###########init###########");
	}

	public Handler.InvocationResponse invoke(MessageContext ctx) throws AxisFault {
		System.out.println(ctx.getEnvelope().toString());
		return Handler.InvocationResponse.CONTINUE;
	}
}
