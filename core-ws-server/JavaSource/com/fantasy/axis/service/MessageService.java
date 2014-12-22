package com.fantasy.axis.service;

import com.fantasy.axis.bean.Message;
import com.fantasy.axis.dao.MessageDao;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service("axis.MessageService")
@Transactional
public class MessageService {

    private static final Log LOG = LogFactory.getLog(MessageService.class);

    @Resource(name = "spring.executor")
    private SchedulingTaskExecutor executor;

    @Autowired
    private MessageDao messageDao;

    public void log(final MessageContext msgContext, final Message.Type type) {
        final MessageService messageService = SpringContextUtil.getBeanByType(MessageService.class);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Message.ResultType result = msgContext.getFLOW() == MessageContext.IN_FAULT_FLOW || msgContext.getFLOW() == MessageContext.OUT_FAULT_FLOW ? Message.ResultType.fault : Message.ResultType.normal;
                Message message;
                if (msgContext.getFLOW() == MessageContext.IN_FLOW || msgContext.getFLOW() == MessageContext.IN_FAULT_FLOW) {
                    if (msgContext.getMessageID() == null) {
                        LOG.error("MessageID is null.");
                        return;
                    }
                    message = new Message();
                    message.setId(msgContext.getMessageID());
                    message.setType(type);
                    message.setRemoteAddr(StringUtil.nullValue(msgContext.getProperty(MessageContext.REMOTE_ADDR)));
                    message.setIn(StringUtil.nullValue(msgContext.getEnvelope()));
                    message.setResult(result);
                } else {
                    if (msgContext.getRelatesTo() == null) {
                        LOG.error("RelatesTo is null.");
                        return;
                    }
                    String messageID = msgContext.getRelatesTo().getValue();
                    message = messageService.get(messageID);
                    for (int i = 0; i <= 3 && message == null; i++) {
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException e) {
                            LOG.debug(e.getMessage());
                        }
                        message = messageService.get(messageID);
                    }
                    if (message == null) {
                        LOG.error(" message is null. ");
                        return;
                    }
                    message.setOut(StringUtil.nullValue(msgContext.getEnvelope()));
                    message.setResult(result);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(message);
                }
                messageService.save(message);
            }
        });
    }

    public Message get(String id) {
        return this.messageDao.get(id);
    }

    public void save(Message message) {
        this.messageDao.save(message);
    }

}
