package com.fantasy.framework.io;

import java.io.IOException;

public interface Connection {

    /**
     * @return {Connection}
     * @throws IOException
     * @功能描述
     */
    public Connection handle() throws IOException;

    public long getTimeStamp();

    /**
     * 是否空闲
     *
     * @return {boolean}
     * @功能描述
     */
    public boolean isIdle();

    /**
     * 是否挂起
     *
     * @return {boolean}
     * @功能描述
     */
    public boolean isSuspended();

    /**
     * 关闭连接
     *
     * @功能描述
     */
    public void closed();

}
