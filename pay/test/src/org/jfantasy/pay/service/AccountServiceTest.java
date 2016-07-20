package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * 账户测试
 */
public class AccountServiceTest {

    private static final Log LOG = LogFactory.getLog(AccountServiceTest.class);

    private PasswordEncoder passwordEncoder =  new StandardPasswordEncoder();

    @Test
    public void encode(){
        String password = passwordEncoder.encode("895973");

        String md5 = DigestUtils.md5DigestAsHex("1111".getBytes());
        LOG.debug(password);

        LOG.debug(md5);
    }

}