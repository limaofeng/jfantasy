package org.jfantasy.website;

import org.jfantasy.website.bean.Data;
import org.jfantasy.website.bean.Page;
import org.jfantasy.website.exception.SwpException;

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

    /**
     * 通过某个具体的pageItem ID和具体的数据重新生成此页面
     * @param pageItemId 页面定义item id
     * @param datas  页面数据集
     * @return Page页面
     * @throws SwpException
     * @throws IOException
     */
    public Page reCreate(Long pageItemId, List<Data> datas) throws SwpException ,IOException;

}
