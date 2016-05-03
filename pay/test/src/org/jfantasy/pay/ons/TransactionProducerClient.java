package org.jfantasy.pay.ons;

import com.alibaba.rocketmq.client.log.ClientLogger;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TransactionProducerClient {
    private final static Logger log = ClientLogger.getLog(); // 用户需要设置自己的log, 记录日志便于排查问题

    public static void main(String[] args) throws InterruptedException {
        final BusinessService businessService = new BusinessService(); // 本地业务Service
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, ""); // ProducerId需要设置您自己的
        properties.put(PropertyKeyConst.AccessKey, ""); // AccessKey 需要设置您自己的
        properties.put(PropertyKeyConst.SecretKey, ""); // SecretKey 需要设置您自己的
        TransactionProducer producer = ONSFactory.createTransactionProducer(properties,
                new LocalTransactionCheckerImpl());
        producer.start();
        Message msg = new Message("topic", "TagA", "Hello ONS transaction===".getBytes());
        // topic需要设置您自己的
        SendResult sendResult = producer.send(msg, new LocalTransactionExecuter() {
            @Override
            public TransactionStatus execute(Message msg, Object arg) {
                // 消息ID(有可能消息体一样，但消息id不一样, 当前消息ID在console控制不可能查询)
                String msgId = msg.getMsgID();
                // 消息体内容进行crc32, 也可以使用其它的如MD5
                long crc32Id = HashUtil.crc32Code(msg.getBody());
                // 消息ID和crc32id主要是用来防止消息重复
                // 如果业务本身是幂等的, 可以忽略, 否则需要利用msgId或crc32Id来做幂等
                // 如果要求消息绝对不重复, 推荐做法是对消息体body使用crc32或md5来防止重复消息.
                Object businessServiceArgs = new Object();
                TransactionStatus transactionStatus = TransactionStatus.Unknow;
                try {
                    boolean isCommit = businessService.execbusinessService(businessServiceArgs);
                    if (isCommit) {
                        // 本地事务成功、提交消息
                        transactionStatus = TransactionStatus.CommitTransaction;
                    } else {
                        // 本地事务失败、回滚消息
                        transactionStatus = TransactionStatus.RollbackTransaction;
                    }
                } catch (Exception e) {
                    log.error("msgId:{}", msgId, e);
                }
                System.out.println(msg.getMsgID());
                log.warn("msgId:{}transactionStatus:{}", msgId, transactionStatus.name());
                return transactionStatus;
            }
        }, null);
        // demo example 防止进程退出(实际使用不需要这样)
        TimeUnit.MILLISECONDS.sleep(Integer.MAX_VALUE);
    }
}