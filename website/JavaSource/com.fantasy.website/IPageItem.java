package org.jfantasy.website;

import org.jfantasy.website.bean.Data;
import org.jfantasy.website.exception.SwpException;

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
