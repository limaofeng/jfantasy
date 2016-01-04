package org.jfantasy.file.job;


import org.jfantasy.framework.util.common.StreamUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Deprecated
public class VideoTranscodingToMp4Job implements Job {

    private static final Log LOGGER = LogFactory.getLog(VideoTranscodingToMp4Job.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String infile = "";
        String outfile = "";
        String mp4 = "ffmpeg -i " + infile + " -vcodec libx264 -profile:v Main " + outfile;
        try {
            Process proc = Runtime.getRuntime().exec(mp4);
            InputStream stderr = proc.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stderr));
            String line;
            while ((line = br.readLine()) != null){
                LOGGER.debug("Process exitValue:" + line);
            }
            int exitVal = proc.waitFor();
            StreamUtil.closeQuietly(stderr);
            LOGGER.debug("Process exitValue:" + exitVal);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
