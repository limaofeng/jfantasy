package org.jfantasy.pay.service;

import org.jfantasy.pay.bean.Project;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.TxChannel;
import org.jfantasy.pay.bean.enums.TxStatus;
import org.jfantasy.pay.dao.ProjectDao;
import org.jfantasy.pay.dao.TransactionDao;
import org.jfantasy.pay.dao.TxLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Properties;

@Service
public class TransactionService {

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private TxLogDao txLogDao;
    @Autowired
    private ProjectDao projectDao;

    /**
     * 第三方支付业务
     *
     * @param project 支付项目
     * @param from    付款方
     * @param to      收款方
     * @param amount  金额
     * @param notes   备注
     * @return Transaction
     */
    @Transactional
    public Transaction thirdparty(Project project, String from, String to, BigDecimal amount, String notes, Properties properties) {
        Transaction transaction = new Transaction();
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setAmount(amount);
        transaction.setNotes(notes);
        transaction.setStatus(TxStatus.apply);
        transaction.setChannel(TxChannel.thirdparty);
        transaction.setProject(project);
        transaction.setProperties(properties);
        //保存交易日志
        transaction = this.transactionDao.save(transaction);
        txLogDao.save(transaction, notes);
        return transaction;
    }

    /**
     * 转账
     *
     * @param from   转出
     * @param to     转入
     * @param amount 金额
     * @param notes  备注
     * @return Transaction
     */
    @Transactional
    public Transaction transfer(String from, String to, BigDecimal amount, String notes) {
        Transaction transaction = new Transaction();
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setAmount(amount);
        transaction.setNotes(notes);
        transaction.setStatus(TxStatus.apply);
        transaction.setChannel(TxChannel.internal);
        transaction.setProject(projectDao.get("transfer"));
        //保存交易日志
        transaction = this.transactionDao.save(transaction);
        txLogDao.save(transaction, notes);
        return transaction;
    }

}
