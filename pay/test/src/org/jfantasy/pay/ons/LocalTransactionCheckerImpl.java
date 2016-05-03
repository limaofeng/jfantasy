package org.jfantasy.pay.ons;

import com.alibaba.rocketmq.client.log.ClientLogger;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import org.slf4j.Logger;

public class LocalTransactionCheckerImpl implements LocalTransactionChecker {
    private final static Logger log = ClientLogger.getLog();
    final  BusinessService businessService = new BusinessService();

    @Override
    public TransactionStatus check(Message msg) {
        //消息ID(有可能消息体一样，但消息id不一样, 当前消息ID在console控制不可能查询)
        String msgId = msg.getMsgID();
        //消息体内容进行crc32, 也可以使用其它的方法如MD5
        long crc32Id = HashUtil.crc32Code(msg.getBody());
        //消息ID、消息本body crc32Id主要是用来防止消息重复
        //如果业务本身是幂等的, 可以忽略, 否则需要利用msgId或crc32Id来做幂等
        //如果要求消息绝对不重复, 推荐做法是对消息体body使用crc32或md5来防止重复消息.
        //业务自己的参数对象, 这里只是一个示例, 实际需要用户根据情况来处理
        Object businessServiceArgs = new Object();
        TransactionStatus transactionStatus = TransactionStatus.Unknow;
        try {
            boolean isCommit = businessService.checkbusinessService(businessServiceArgs);
            if (isCommit) {
                //本地事务已成功、提交消息
                transactionStatus = TransactionStatus.CommitTransaction;
            } else {
                //本地事务已失败、回滚消息
                transactionStatus = TransactionStatus.RollbackTransaction;
            }
        } catch (Exception e) {
            log.error("msgId:{}", msgId, e);
        }
        log.warn("msgId:{}transactionStatus:{}", msgId, transactionStatus.name());
        return transactionStatus;
    }
}