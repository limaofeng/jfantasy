package com.fantasy.framework.ws.axis2.handlers;

import com.fantasy.axis.bean.Message;
import com.fantasy.axis.service.MessageService;
import com.fantasy.framework.spring.SpringContextUtil;
import org.apache.axiom.util.UIDGenerator;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;

public class LogHandler extends AbstractHandler implements Handler {

    private MessageService messageService;

    public Handler.InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        //如果没有 MessageID, 设置一个新的id
        if (msgContext.getFLOW() == MessageContext.IN_FLOW && msgContext.getMessageID() == null) {
            msgContext.setMessageID(UIDGenerator.generateURNString());
        }
        if (messageService == null) {
            messageService = SpringContextUtil.getBeanByType(MessageService.class);
        }
        messageService.log(msgContext, Message.Type.server);
        return Handler.InvocationResponse.CONTINUE;
    }

}