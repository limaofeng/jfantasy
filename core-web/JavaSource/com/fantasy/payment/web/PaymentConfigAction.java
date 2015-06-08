package com.fantasy.payment.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentConfigService;
import com.fantasy.payment.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentConfigAction extends ActionSupport {

    private static final long serialVersionUID = 2346365431436263753L;

    @Autowired
    private PaymentConfigService configService;
    @Autowired
    private PaymentService paymentService;

    /**
     * 支持测试方法
     *
     * @return {String}
     */
    public String test(Long paymentConfigId) throws IOException, PaymentException {
        this.attrs.put("sHtmlText", this.paymentService.test(paymentConfigId, WebUtil.getParameterMap(this.request)));
        return SUCCESS;
    }

    /**
     * 首页
     *
     * @return {String}
     */
    public String index() {
        this.search(new Pager<PaymentConfig>(), new ArrayList<PropertyFilter>());
        this.attrs.get(ROOT);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 搜索
     *
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return {String}
     */
    public String search(Pager<PaymentConfig> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.configService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 保存
     *
     * @param config 支付配置
     * @return {String}
     */
    public String save(PaymentConfig config) {
        this.attrs.put("paymentConfig", this.configService.save(config));
        return JSONDATA;
    }

    /**
     * 修改
     *
     * @param id 配置id
     * @return {String}
     */
    public String edit(Long id) {
        this.attrs.put("paymentConfig", this.configService.get(id));
        return SUCCESS;
    }

    /**
     * 显示
     *
     * @param id 配置id
     * @return {String}
     */
    public String view(Long id) {
        this.attrs.put("paymentConfig", this.configService.get(id));
        return SUCCESS;
    }

    /**
     * 批量删除
     *
     * @param ids 配置ids
     * @return {String}
     */
    public String delete(Long[] ids) {
        this.configService.delete(ids);
        return JSONDATA;
    }

}

