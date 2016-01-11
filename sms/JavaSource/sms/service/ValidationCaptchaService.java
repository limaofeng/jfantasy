package org.jfantasy.sms.service;

import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.regexp.RegexpCst;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.sms.SMSService;
import org.jfantasy.sms.bean.Captcha;
import org.jfantasy.sms.bean.CaptchaConfig;
import org.jfantasy.sms.bean.LogMobile;
import org.jfantasy.sms.dao.CaptchaDao;
import org.jfantasy.sms.dao.LogMobileDao;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 短信服务类
 * <p/>
 * <p/>
 * 短信验证码功能要求:<br/>
 * 1.可以配置短信码的长度。<br/>
 * 2.可以配置短信模板。<br/>
 * 3.可以配置短信过期时间。（短信重复发送时，将重发上次生成的验证码）<br/>
 * 4.可以配置短信验证的过期时间。<br/>
 * 5.线程定时清理无效的验证码。<br/>
 * 6.验证后成功清除原验证码。<br/>
 * 7.可以配置验证失败次数。（次数耗尽清除验证码）<br/>
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-4 上午10:27:34
 */
@Transactional
public class ValidationCaptchaService {

    /**
     * 短信发送类
     */
    private SMSService smsService;

    private Handlebars handlebars = new Handlebars();

    private static final Log LOG = LogFactory.getLog(ValidationCaptchaService.class);

    @Autowired
    private CaptchaDao captchaDao;
    @Autowired
    private CaptchaConfigService captchaConfigService;
    @Autowired
    private LogMobileDao logMobileDao;

    /**
     * 验证短信验证码
     *
     * @param configId 配置id
     * @param id       发送id
     * @param code     验证码
     * @return {boolean}
     */
    public boolean validateResponseForID(String configId, String id, String code) {
        CaptchaConfig config = this.captchaConfigService.get(configId);
        // 配置信息不存在
        if (config == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
            }
            return false;
        }
        Captcha captcha = captchaDao.findUnique(Restrictions.eq("captchaConfig.id", configId), Restrictions.eq("sessionId", id));
        // 验证码不存在或者超过重试次数
        if (captcha == null || captcha.getRetry() >= config.getRetry()) {
            return false;
        }
        // 验证码已经过期
        if (DateUtil.interval(DateUtil.now(), captcha.getModifyTime(), Calendar.SECOND) > config.getExpires()) {
            return false;
        }
        // 验证码匹配错误
        if (!code.equalsIgnoreCase(captcha.getValue())) {
            captcha.setRetry(captcha.getRetry() + 1);
            captchaDao.save(captcha);
            return false;
        }
        captchaDao.delete(captcha);
        return true;
    }

    /**
     * 发送短信验证码
     *
     * @param configId 配置ID
     * @param id       发送id
     * @param phone    手机号
     */
    public String getChallengeForID(String configId, String id, String phone) {
        if (!RegexpUtil.isMatch(phone, RegexpCst.VALIDATOR_MOBILE)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("发送的手机号码格式不对:" + phone);
            }
            throw new IgnoreException("发送的手机号码格式不对:" + phone);
        }
        CaptchaConfig config = this.captchaConfigService.get(configId);
        if (config == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
            }
            throw new IgnoreException("短信验证设置[id=" + configId + "]不存在!");
        }
        Captcha captcha = captchaDao.findUnique(Restrictions.eq("captchaConfig.id", configId), Restrictions.eq("sessionId", id));
        CaptchaConfig captchaConfig = captchaConfigService.get(configId);
        if (captcha == null) {
            captcha = new Captcha();
            captcha.setSessionId(id);
            captcha.setCaptchaConfig(captchaConfig);
            captcha.setValue(captchaConfigService.getWordGenerator(config.getRandomWord()).getWord(config.getWordLength()));
        } else if (DateUtil.interval(DateUtil.now(), captcha.getModifyTime(), Calendar.SECOND) > config.getActive()) {// 如果验证码超过有效期限，重新生成新的验证码
            captcha.setValue(captchaConfigService.getWordGenerator(config.getRandomWord()).getWord(config.getWordLength()));
            captcha.setRetry(0);
        } else if (captcha.getRetry() >= config.getRetry()) {  // 如果验证码尝试次数已经超过允许次数，重新生成新的验证码
            captcha.setValue(captchaConfigService.getWordGenerator(config.getRandomWord()).getWord(config.getWordLength()));
            captcha.setRetry(0);
        }
        captcha = captchaDao.save(captcha);
        AtomicReference<Map<String, Object>> data = new AtomicReference<Map<String, Object>>(new HashMap<String, Object>());
        data.get().put("config", config);
        data.get().put("captcha", captcha);
        boolean status = false;
        String content = "";
        try {
            Template template = handlebars.compileInline(captchaConfig.getTemplate());
            status = smsService.send(phone, template.apply(data.get()));
        } catch (IOException e) {
            LOG.debug(e.getMessage());
        } finally {
            LogMobile logMobile = new LogMobile();
            logMobile.setCaptchaConfig(captchaConfig);
            logMobile.setContent(content);
            logMobile.setMobilephone(phone);
            logMobile.setStatus(status);
            logMobile.setDescription("验证码[" + captcha.getValue() + "]配置信息[" + config.getId() + "]");
            logMobileDao.save(logMobile);
        }
        return captcha.getValue();
    }

    public void setSmsService(SMSService smsService) {
        this.smsService = smsService;
    }

}
