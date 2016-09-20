package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.ProjectType;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.rest.models.PayForm;
import org.jfantasy.pay.rest.models.assembler.TransactionResourceAssembler;
import org.jfantasy.pay.service.*;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/** 交易记录 **/
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionResourceAssembler assembler = new TransactionResourceAssembler();

    private final TransactionService transactionService;
    private final PayService payService;
    private final ProjectService projectService;
    private final PayConfigService configService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(PayConfigService configService, PayService payService, ProjectService projectService, TransactionService transactionService, AccountService accountService) {
        this.configService = configService;
        this.payService = payService;
        this.projectService = projectService;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    /** 查询交易记录 **/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> seach(Pager<Transaction> pager, List<PropertyFilter> filters) {
        return assembler.toResources(transactionService.findPager(pager, filters));
    }

    /** 创建交易 **/
    @RequestMapping(method = RequestMethod.POST)
    @JsonResultFilter(allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "pay_product_id", "name", "platforms", "default"}))
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@RequestBody Transaction transaction) {
        transaction.setProject(this.projectService.get(transaction.getProject().getKey()));
        return transform(transactionService.save(transaction));
    }

    //transactionService.transfer(tx.getFrom(), tx.getTo(), tx.getAmount(), tx.getNotes());
    /** 获取交易详情 **/
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @JsonResultFilter(allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "pay_product_id", "name", "platforms", "default"}))
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String sn) {
        return transform(get(sn));
    }

    /** 获取支付表单进行支付 **/
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/pay-form")
    @ResponseBody
    @JsonResultFilter(allow = @AllowProperty(pojo = Payment.class, name = {"sn", "type", "pay_config_name", "total_amount", "payment_fee", "status", "source"}))
    public ToPayment payForm(@PathVariable("id") String sn, @RequestBody PayForm payForm) {
        Transaction transaction = get(sn);
        return payService.pay(transaction, payForm.getPayconfigId(), payForm.getPayType(), payForm.getPayer(), payForm.getProperties());
    }

    private Transaction get(String id) {
        return transactionService.get(id);
    }

    private ResultResourceSupport transform(Transaction transaction) {
        ResultResourceSupport resource = assembler.toResource(transaction);
        OAuthUserDetails user = SpringSecurityUtils.getCurrentUser(OAuthUserDetails.class);

        if (user == null) {
            return resource;
        }

        if (transaction.getProject().getType() == ProjectType.order) {
            switch (transaction.get(Transaction.STAGE)) {
                case Transaction.STAGE_PAYMENT:

                    if (!ThreadJacksonMixInHolder.getMixInHolder().isReturnProperty(PayConfig.class, "payconfigs")) {
                        break;
                    }

                    List<PayConfig> payconfigs = configService.find();
                    Iterator<PayConfig> iterator = payconfigs.iterator();

                    // 按平台过滤掉不能使用的支付方式
                    while (iterator.hasNext()) {
                        PayConfig payConfig = iterator.next();
                        if (!payConfig.getPlatforms().contains(user.getPlatform())) {
                            iterator.remove();
                        }
                    }
                    // 判断余额支付，金额是否可以支付
                    PayConfig payConfig = ObjectUtil.find(payconfigs, "payProductId", "walletpay");
                    if (payConfig != null && accountService.get(transaction.getFrom()).getAmount().compareTo(transaction.getAmount()) < 0) {
                        payconfigs.remove(payConfig);
                        payConfig.setDisabled(true);
                        payconfigs.add(payConfig);
                    }
                    // 设置默认支付方式
                    String payment_sn = transaction.get("payment_sn");
                    if (StringUtil.isNotBlank(payment_sn)) {
                        Payment payment = ObjectUtil.find(transaction.getPayments(), "sn", payment_sn);
                        if (payment.getStatus() == PaymentStatus.ready) { // 加载支付方式
                            payConfig = ObjectUtil.find(payconfigs, "payProductId", payment.getPayConfig().getPayProductId());
                            if (payConfig.getDisabled() != Boolean.TRUE) {
                                payConfig.setDefault(true);
                            }
                        }
                    } else if (!payconfigs.isEmpty()) {
                        payconfigs.get(0).setDefault(true);
                    }
                    resource.set("payconfigs", payconfigs);
                    break;
                case Transaction.STAGE_REFUND:
                    break;
            }
        }
        return resource;
    }

}
