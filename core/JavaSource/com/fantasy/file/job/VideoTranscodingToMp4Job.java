package com.fantasy.file.job;


import com.fantasy.framework.util.common.StreamUtil;
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

    private static final Log logger = LogFactory.getLog(VideoTranscodingToMp4Job.class);

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
                logger.debug("Process exitValue:" + line);
            }
            int exitVal = proc.waitFor();
            StreamUtil.closeQuietly(stderr);
            logger.debug("Process exitValue:" + exitVal);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
