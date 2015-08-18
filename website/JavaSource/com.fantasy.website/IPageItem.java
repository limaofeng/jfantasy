package com.fantasy.website;

import com.fantasy.website.bean.Data;
import com.fantasy.website.exception.SwpException;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface IPageItem {

    /**
     * 刷新
     * @throws SwpException
     */
    public void refash() throws SwpException, IOException;

    /**
     * 刷新
     * @param datas 数据
     * @throws SwpException
     */
    public void refash(List<Data> datas) throws SwpException;

}
