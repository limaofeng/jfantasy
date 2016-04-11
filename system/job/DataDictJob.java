package org.jfantasy.system.job;

import org.jfantasy.filestore.FileManager;
import org.jfantasy.filestore.service.FileManagerFactory;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.system.service.DataDictionaryService;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DataDictJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(DataDictJob.class);

    private static Handlebars handlebars = new Handlebars();
    private static Template template;

    static {
        try {
            handlebars.registerHelper("compare", new Helper<Object>() {
                @Override
                public CharSequence apply(Object context, Options options) throws IOException {
                    if(options.params[0].equals(context == "last")){
                        return options.fn();
                    }else{
                        return options.inverse();
                    }
                }
            });
            template = handlebars.compile("/org/jfantasy/system/job/dicts");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            final DataDictionaryService dictionaryService = SpringContextUtil.getBeanByType(DataDictionaryService.class);
            getFileManager().writeFile("/static/data/data-dict-type.json", new ByteArrayInputStream(JSON.serialize(dictionaryService.allTypes()).getBytes("UTF-8")));
            getFileManager().writeFile("/static/data/data-dict.json", new ByteArrayInputStream(JSON.serialize(dictionaryService.allDataDicts()).getBytes("UTF-8")));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("生成:/static/data/data-dict-type.json 与 /static/data/data-dict.json 成功!");
            }
            assert template != null;
            StringWriter writer = new StringWriter();
            template.apply(new HashMap<String, Object>() {
                {
                    this.put("dds", dictionaryService.allDataDicts());
                }
            },writer);
            getFileManager().writeFile("/static/dds.js", new ByteArrayInputStream(writer.getBuffer().toString().getBytes()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("static-access")
    private FileManager getFileManager() {
        FileManager fileManager = FileManagerFactory.getWebRootFileManager();
        while (fileManager == null) {
            try {
                Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(100));
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            fileManager = FileManagerFactory.getWebRootFileManager();
        }
        return fileManager;
    }

}
