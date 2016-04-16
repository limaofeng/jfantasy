package org.jfantasy.pay.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.jfantasy.file.FileItem;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;

import java.util.Properties;

/**
 * 基类 - 支付产品
 */
public abstract class PayProductSupport implements PayProduct {

    protected final static Log LOG = LogFactory.getLog(PayProductSupport.class);

    protected String id;//支付产品ID
    protected String name;// 支付产品名称
    protected String bargainorIdName;// 商户ID参数名称
    protected String description;// 支付产品描述
    protected String bargainorKeyName;// 密钥参数名称
    protected String shroffAccountName;//收款方账号名称
    protected CurrencyType[] currencyTypes;// 支持货币类型
    protected String logoPath;// 支付产品LOGO路径

    @Override
    public Refund refund(Refund refund) {
        throw new PayException(this.getName() + " 的退款逻辑未实现");
    }

    public String wap(){
        throw new PayException(this.getName() + " 的 wap 支付未实现");
    }

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        throw new PayException(this.getName() + " 的 app 支付未实现");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @Override
    public String getShroffAccountName() {
        return shroffAccountName;
    }

    public void setShroffAccountName(String shroffAccountName) {
        this.shroffAccountName = shroffAccountName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBargainorIdName() {
        return bargainorIdName;
    }

    public void setBargainorIdName(String bargainorIdName) {
        this.bargainorIdName = bargainorIdName;
    }

    public String getBargainorKeyName() {
        return bargainorKeyName;
    }

    public void setBargainorKeyName(String bargainorKeyName) {
        this.bargainorKeyName = bargainorKeyName;
    }

    public CurrencyType[] getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(CurrencyType[] currencyTypes) {
        this.currencyTypes = currencyTypes;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected static FileItem loadFileItem(FileDetail fileDetail) {
        FileService fileService = SpringContextUtil.getBeanByType(FileService.class);
        assert fileService != null;
        FileDetail realFileDetail = fileService.get(FileDetailKey.newInstance(fileDetail.getAbsolutePath(), fileDetail.getFileManagerId()));
        return FileManagerFactory.getInstance().getFileManager(fileDetail.getFileManagerId()).getFileItem(realFileDetail.getRealPath());
    }

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        return null;
    }

    public void log(String tyep, String payType, Payment payment, PayConfig config, String result) {
        MDC.put("type", tyep);
        MDC.put("payType", payType);
        MDC.put("paymentSn", payment.getSn());
        MDC.put("payProductId", config.getPayProductId());
        MDC.put("payConfigId", config.getId());
        MDC.put("body", result);
        LOG.info(MDC.getContext());
    }

    public void log(String tyep, String payType, Refund refund, PayConfig config, String result) {
        MDC.put("type", tyep);
        MDC.put("payType", payType);
        MDC.put("paymentSn", refund.getSn());
        MDC.put("payProductId", config.getPayProductId());
        MDC.put("payConfigId", config.getId());
        MDC.put("body", result);
        LOG.info(MDC.getContext());
    }

}