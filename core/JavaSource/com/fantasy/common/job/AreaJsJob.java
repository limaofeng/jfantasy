package com.fantasy.common.job;

import com.fantasy.common.service.AreaService;
import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AreaJsJob implements Job {

    private static final Logger logger = Logger.getLogger(AreaJsJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            AreaService areaService = SpringContextUtil.getBeanByType(AreaService.class);
            getFileManager().writeFile("/static/data/area.json",new ByteArrayInputStream(JSON.serialize(areaService.allAreas()).getBytes()));
            if (logger.isDebugEnabled()) {
                logger.debug("生成:/static/js/area.json成功!");
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
