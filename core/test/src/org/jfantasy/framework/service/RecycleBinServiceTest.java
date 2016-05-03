package org.jfantasy.framework.service;

import org.junit.Test;

/**
 * Created by limaofeng on 16/4/21.
 */
public class RecycleBinServiceTest {

    @Test
    public void recycle() throws Exception {
        RecycleBinService recycleBinService = new RecycleBinService();
        recycleBinService.recycle(new Object());
    }

    @Test
    public void getHistorys() throws Exception {
        //TODO 待测试
    }

}