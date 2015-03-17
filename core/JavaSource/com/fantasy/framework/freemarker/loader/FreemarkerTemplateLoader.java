package com.fantasy.framework.freemarker.loader;

import com.fantasy.swp.SwpContext;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.service.TemplateService;
import com.fantasy.system.bean.Website;
import freemarker.cache.TemplateLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by wml on 2015/1/26.
 */
public class FreemarkerTemplateLoader implements TemplateLoader {

    private final static Log logger = LogFactory.getLog(FileManagerTemplateLoader.class);

    @Autowired
    private TemplateService templateService;

    @Override
    public Object findTemplateSource(String name) throws IOException {
        Website website = SwpContext.getContext().getWebsite();
        Template template = this.templateService.findUniqueByPath(name.startsWith("/") ? name : ("/" + name),website.getId());
        if(template == null){
            return null;
        }
        return template.getId();
    }

    @Override
    public long getLastModified(Object templateSource) {
        Template template = templateService.get((Long)templateSource);
        return template.getModifyTime().getTime();
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Template template = templateService.get((Long)templateSource);
        return new InputStreamReader(new ByteArrayInputStream(template.getContent().getBytes()), encoding);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

    }

}
