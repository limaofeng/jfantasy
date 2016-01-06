package org.jfantasy.common.job;

import org.jfantasy.common.service.AreaService;
import org.jfantasy.file.FileManager;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AreaJsJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(AreaJsJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            AreaService areaService = SpringContextUtil.getBeanByType(AreaService.class);
            getFileManager().writeFile("/static/data/area.json",new ByteArrayInputStream(JSON.serialize(areaService.allAreas()).getBytes()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("生成:/static/js/area.json成功!");
            }
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
