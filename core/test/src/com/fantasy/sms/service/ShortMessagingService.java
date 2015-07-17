package com.fantasy.sms.service;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.sms.SMSService;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.regexp.RegexpCst;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.sms.bean.Captcha;
import com.fantasy.sms.bean.CaptchaConfig;
import com.fantasy.sms.bean.LogMobile;
import com.fantasy.sms.dao.CaptchaDao;
import com.fantasy.sms.dao.LogMobileDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 短信服务类
 * 
 * @功能描述 <br/>
 *       短信验证码功能要求:<br/>
 *       1.可以配置短信码的长度。<br/>
 *       2.可以配置短信模板。<br/>
 *       3.可以配置短信过期时间。（短信重复发送时，将重发上次生成的验证码）<br/>
 *       4.可以配置短信验证的过期时间。<br/>
 *       5.线程定时清理无效的验证码。<br/>
 *       6.验证后成功清除原验证码。<br/>
 *       7.可以配置验证失败次数。（次数耗尽清除验证码）<br/>
 * @author 李茂峰
 * @since 2013-7-4 上午10:27:34
 * @version 1.0
 */
public class ShortMessagingService {

	/**
	 * 短信发送类
	 */
	private SMSService smsService;

	private static final Log logger = LogFactory.getLog(ShortMessagingService.class);

	@Autowired
	private CaptchaDao captchaDao;
	@Autowired
	private CaptchaConfigService captchaConfigService;
	@Autowired
	private LogMobileDao logMobileDao;

	/**
	 * 验证短信验证码
	 * 
	 * @功能描述
	 * @param configId 配置id
	 * @param id 发送id
	 * @param code 验证码
	 * @return {boolean}
	 */
	public boolean validateResponseForID(String configId, String id, String code) {
		CaptchaConfig config = this.captchaConfigService.get(configId);
		// 配置信息不存在
		if (configId == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
			}
			return false;
		}
		Captcha captcha = captchaDao.findUnique(Restrictions.eq("captchaConfig.id", configId), Restrictions.eq("sessionId", id));
		// 验证码不存在或者超过重试次数
		if (captcha == null || captcha.getRetry() >= config.getRetry()) {
            return false;
        }
		// 验证码已经过期
		if (DateUtil.interval(DateUtil.now(), captcha.getModifyTime(), Calendar.MILLISECOND) > config.getExpires()) {
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
	 * @param configId
	 * @param id
	 * @param phone
	 * @return
	 */
	public String getChallengeForID(String configId, String id, String phone) {
		if (!RegexpUtil.isMatch(phone, RegexpCst.VALIDATOR_MOBILE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("发送的手机号码格式不对:" + phone);
			}
			throw new IgnoreException("发送的手机号码格式不对:" + phone);
		}
		CaptchaConfig config = this.captchaConfigService.get(configId);
		if (configId == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
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
		} else if (DateUtil.interval(DateUtil.now(), captcha.getModifyTime(), Calendar.MILLISECOND) > config.getActive()) {// 如果验证码超过有效期限，重新生成新的验证码
			captcha.setValue(captchaConfigService.getWordGenerator(config.getRandomWord()).getWord(config.getWordLength()));
            captcha.setRetry(0);
		} else if(captcha.getRetry()>=config.getRetry()) {  // 如果验证码尝试次数已经超过允许次数，重新生成新的验证码
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
			content = RegexpUtil.replace(captchaConfig.getTemplate(),"\\{captcha\\}",captcha.getValue());
			status = smsService.send(phone, content);
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
