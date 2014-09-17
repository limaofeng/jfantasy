package com.fantasy.system.job;

import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.system.service.DataDictionaryService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DataDictJob implements Job {

    private static final Logger logger = Logger.getLogger(DataDictJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            DataDictionaryService dictionaryService = SpringContextUtil.getBeanByType(DataDictionaryService.class);
            getFileManager().writeFile("/static/data/data-dict-type.json", new ByteArrayInputStream(JSON.serialize(dictionaryService.allTypes()).getBytes()));
            getFileManager().writeFile("/static/data/data-dict.json", new ByteArrayInputStream(JSON.serialize(dictionaryService.allDataDicts()).getBytes()));
            if (logger.isDebugEnabled()) {
                logger.debug("生成:/static/data/data-dict-type.json 与 /static/data/data-dict.json 成功!");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("static-access")
    private FileManager getFileManager() {
        FileManager fileManager = FileManagerFactory.getWebRootFileManager();
        while (fileManager == null) {
            try {
                Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(100));
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            fileManager = FileManagerFactory.getWebRootFileManager();
        }
        return fileManager;
    }

}
