package org.jfantasy.framework.comet;

import org.jfantasy.framework.util.common.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable {

    private final static Log LOG = LogFactory.getLog(Consumer.class);

    private BlockingQueue<String> drop;

    public Consumer(BlockingQueue<String> d) {
        this.drop = d;
    }

    public void run() {
        try {
            String msg = null;
            while (!"DONE".equals(msg = (String) this.drop.take())) {
                Thread.sleep(1500L);
                LOG.debug(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS") + "\t" + msg);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}