package com.fantasy.framework.dao.mybatis.keygen;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.MessageDigestUtil;
import ognl.Ognl;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.sql.Statement;
import java.util.Random;

/**
 * GUID序列生成器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:09:32
 */
public class GUIDKeyGenerator implements KeyGenerator, InitializingBean {
    private static final Logger LOGGER = Logger.getLogger(GUIDKeyGenerator.class);
    private Random random;
    private String s_id;

    private static GUIDKeyGenerator instance;

    public synchronized static GUIDKeyGenerator getInstance() {
        if (instance == null && SpringContextUtil.getApplicationContext() != null) {
            instance = SpringContextUtil.getBeanByType(GUIDKeyGenerator.class);
        }
        if (instance == null) {
            instance = new GUIDKeyGenerator();
            instance.afterPropertiesSet();
        }
        return instance;
    }

    private GUIDKeyGenerator() {
    }

    public void afterPropertiesSet() {
        SecureRandom secureRandom = new SecureRandom();
        long secureInitializer = secureRandom.nextLong();
        random = new Random(secureInitializer);
        try {
            s_id = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            LOGGER.error(e);
        }
    }

    public String pretty(String md5str) {
        String raw = md5str.toUpperCase();
        StringBuilder sb = new StringBuilder(64);
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成的GUID:" + sb.toString());
        }
        return sb.toString();
    }

    public String getGUID() {
        StringBuilder sbValueBeforeMD5 = new StringBuilder(128);
        long time = System.currentTimeMillis();
        long rand;
        rand = random.nextLong();
        sbValueBeforeMD5.append(s_id);
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(time));
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(rand));
        return pretty(MessageDigestUtil.getInstance().get(sbValueBeforeMD5.toString()));
    }

    public void processBefore(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
        String[] keyProperties = paramMappedStatement.getKeyProperties();
        try {
            Ognl.setValue(keyProperties[0], paramObject, getGUID());
        } catch (Exception e) {
            LOGGER.error("自动设置ID失败", e);
        }
    }

    public void processAfter(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
    }
}