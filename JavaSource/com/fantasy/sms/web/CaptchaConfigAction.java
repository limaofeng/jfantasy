package com.fantasy.sms.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.sms.bean.Captcha;
import com.fantasy.sms.bean.CaptchaConfig;
import com.fantasy.sms.bean.LogMobile;
import com.fantasy.sms.service.CaptchaConfigService;
import com.fantasy.sms.service.CaptchaService;
import com.fantasy.sms.service.LogMobileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 短信配置
 */
public class CaptchaConfigAction extends ActionSupport {

    @Autowired
    private CaptchaConfigService captchaConfigService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private LogMobileService logMobileService;

	/**
	 * 首页
	 * 
	 * @return
	 */
	public String index() {
		this.search();
		this.attrs.put("list", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 搜索列表
	 * 
	 * @return
	 */
	public String search() {
		this.attrs.put(ROOT, this.captchaConfigService.getAll());
		return JSONDATA;
	}

    public String delete(String[] ids){
        this.captchaConfigService.delete(ids);
        return JSONDATA;
    }

    /**
     * 保存
     * @return
     */
    public String save(CaptchaConfig captchaConfig){
        this.captchaConfigService.save(captchaConfig);
        return JSONDATA;
    }

    /**
     * 验证码日志
     * @param pager
     * @param filters
     * @return
     */
    public String captchaLog(Pager<Captcha> pager, List<PropertyFilter> filters){
        this.captchaLogSearch(pager,filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 验证码日志查询
     * @param pager
     * @param filters
     * @return
     */
    public String captchaLogSearch(Pager<Captcha> pager, List<PropertyFilter> filters){
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }

        this.attrs.put(ROOT,this.captchaService.findPager(pager,filters));
        return JSONDATA;
    }

    /**
     * 消息日志
     * @param pager
     * @param filters
     * @return
     */
    public String messageLog(Pager<LogMobile> pager, List<PropertyFilter> filters){
        this.messageLogSearch(pager,filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 消息日志查询
     * @param pager
     * @param filters
     * @return
     */
    public String messageLogSearch(Pager<LogMobile> pager, List<PropertyFilter> filters){
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }

        this.attrs.put(ROOT,this.logMobileService.findPager(pager,filters));
        return JSONDATA;
    }
}
