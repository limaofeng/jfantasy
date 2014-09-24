package com.fantasy.sms.service;

import com.fantasy.sms.bean.CaptchaConfig;
import com.fantasy.sms.dao.CaptchaConfigDao;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CaptchaConfigService {

	private static final Log logger = LogFactory.getLog(CaptchaConfigService.class);
	
	@Resource
	private CaptchaConfigDao captchaConfigDao;

	private ConcurrentMap<String, WordGenerator> wordGeneratorCache = new ConcurrentHashMap<String, WordGenerator>();
    private Object all;

    protected WordGenerator getWordGenerator(String randomWord) {
		if (!wordGeneratorCache.containsKey(randomWord)) {
			if (logger.isDebugEnabled()) {
				logger.debug("缓存验证码生成器:" + randomWord);
			}
			wordGeneratorCache.put(randomWord, new RandomWordGenerator(randomWord));
		}
		return wordGeneratorCache.get(randomWord);
	}
	
	protected WordGenerator removeWordGenerator(String randomWord) {
		return wordGeneratorCache.remove(randomWord);
	}

	public CaptchaConfig get(String configId) {
		return captchaConfigDao.get(configId);
	}

    /**
     * 获取所有短信配置
     * @return
     */
    public List<CaptchaConfig> getAll() {
        return this.captchaConfigDao.find();
    }

    /**
     * 保存短信配置
     * @param captchaConfig
     * @return
     */
    public CaptchaConfig save(CaptchaConfig captchaConfig) {
        this.captchaConfigDao.save(captchaConfig);
        return captchaConfig;
    }

    /**
     * 删除
     * @param ids
     */
    public void delete(String[] ids) {
        for (String id : ids) {
            this.captchaConfigDao.delete(id);
        }
    }

}
