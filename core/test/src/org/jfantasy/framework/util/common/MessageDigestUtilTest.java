package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MessageDigestUtilTest {

    private final static Log LOG = LogFactory.getLog(MessageDigestUtilTest.class);

    @Test
    public void testGet() throws Exception {
        String md5code = DigestUtils.md5DigestAsHex(new FileInputStream(new File(PathUtil.classes() + "/backup/testconfig/log4j.xml")));
        LOG.debug("文件获取MD5码:" + md5code);
        InputStream input = new FileInputStream(new File(PathUtil.classes() + "/backup/testconfig/log4j.xml"));
        md5code = DigestUtils.md5DigestAsHex(input);
        LOG.debug("文件流获取MD5码:" + md5code);
    }
}