package com.fantasy.swp;

import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.exception.SwpException;

import java.io.IOException;
import java.util.List;

/**
 * Created by wuzhiyong on 2015/3/2.
 */
public interface IGenerate {

    /**
     * 创建新的page
     * @param page
     * @return
     * @throws SwpException
     */
    public Page create(Page page) throws SwpException;

    /**
     * 通过某个具体的pageItem ID重新生成此页面
     * @param pageItemId
     * @return
     * @throws SwpException
     */
    public Page reCreate(Long pageItemId) throws SwpException, IOException;

    public Page reCreate(Long pageItemId, List<Data> datas) throws SwpException ,IOException;

}
