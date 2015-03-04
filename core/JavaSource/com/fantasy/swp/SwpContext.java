package com.fantasy.swp;

import com.fantasy.system.bean.Website;

/**
 * swp 上下文
 */
public class SwpContext {

    private static ThreadLocal<SwpContext> threadLocal = new ThreadLocal<SwpContext>();

    /**
     * 站点
     */
    private static Website website;

    public static void setContext(SwpContext swpContext){
        threadLocal.set(swpContext);
    }

    public static SwpContext getContext(){
        return threadLocal.get();
    }

    public static Website getWebsite() {
        return website;
    }

    public static void setWebsite(Website websit) {
        website = websit;
    }
}
