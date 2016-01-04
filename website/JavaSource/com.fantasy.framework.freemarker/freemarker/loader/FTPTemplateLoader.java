package org.jfantasy.framework.freemarker.loader;

import freemarker.cache.TemplateLoader;
import org.jfantasy.framework.service.FTPService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * TODO FTPTemplateLoader 将ftl模板单独存放到一个 ftpserver 中
 */
public class FTPTemplateLoader implements TemplateLoader {

    private FTPService ftpService;

    public void closeTemplateSource(Object templateSource) throws IOException {
    }

    public Object findTemplateSource(String name) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ftpService.download(name, out);
        return out.toString();
    }

    public long getLastModified(Object arg0) {
        return 0;
    }

    public Reader getReader(Object templateSource, final String encoding) throws IOException {
        return null;
    }

    public void setFtpService(FTPService ftpService) {
        this.ftpService = ftpService;
    }

}
