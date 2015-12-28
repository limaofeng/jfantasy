package org.jfantasy.pay.rest;

import com.fantasy.common.order.Order;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.mvc.error.NotFoundException;
import com.fantasy.member.bean.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "payments", description = "支付记录")
@RestController
@RequestMapping("/system/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation("查询支付记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Payment> search(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentService.findPager(pager, filters);
    }

    @ApiOperation("获取支付记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Payment view(@PathVariable("id") Long id) {
        return this.paymentService.get(id);
    }

    @ApiOperation("支付记录对应的支付配置信息")
    @RequestMapping(value = "/{id}/payconfig", method = RequestMethod.GET)
    @ResponseBody
    public PayConfig payconfig(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.get(id);
        if (payment == null) {
            throw new NotFoundException("[id=" + id + "]对应的支付记录未找到");
        }
        return payment.getPayConfig();
    }

    @ApiOperation("支付记录对应的会员信息")
    @RequestMapping(value = "/{id}/member", method = RequestMethod.GET)
    @ResponseBody
    public Member member(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.get(id);
        if (payment == null) {
            throw new NotFoundException("[id=" + id + "]对应的支付记录未找到");
        }
        return payment.getMember();
    }

    @ApiOperation(value = "支付记录对应的订单信息", notes = "支付记录对应的订单信息")
    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    @ResponseBody
    public Order order(@PathVariable("id") Long id) {
        return this.paymentService.getOrderByPaymentId(id);
    }

    @ApiOperation("删除支付记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.paymentService.delete(id);
    }

    @ApiOperation("批量删除支付记录")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... ids) {
        this.paymentService.delete(ids);
    }

}
