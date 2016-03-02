package org.jfantasy.aliyun.oss;

import org.jfantasy.file.bean.enums.FileManagerType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class OSSFileManagerBuilderTest {

    private final static Log LOG = LogFactory.getLog(OSSFileManagerBuilderTest.class);

    private OSSFileManagerBuilder builder = new OSSFileManagerBuilder();

    @Test
    public void testGetType() throws Exception {
        LOG.debug(builder.getType());
        assert builder.getType() == FileManagerType.oss;
    }

    @Test
    public void testRegister() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("accessKeyId", "GjYnEEMsLVTomMzF");
        params.put("accessKeySecret", "rYSFhN67iXR0vl0pUSatSQjEqR2e2F");
        params.put("endpoint", "http://oss-cn-hangzhou.aliyuncs.com");
        params.put("bucketName", "static-jfantasy-org");
        OSSFileManager fileManager = builder.register(params);
        LOG.debug(fileManager);
    }

}