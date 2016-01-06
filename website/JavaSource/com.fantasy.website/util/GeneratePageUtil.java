package org.jfantasy.website.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.website.bean.Data;
import org.jfantasy.website.bean.DataInferface;
import org.jfantasy.website.service.HqlService;
import org.jfantasy.website.service.SpelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzhiyong on 2015/2/5.
 */
public class GeneratePageUtil {

    public static Object getValue(Data data){
        DataInferface dataInferface = data.getDataInferface();
        if(data.getDataSource()==Data.DataSource.stat){   // 数据源为静态
            if(dataInferface.getDataType()==DataInferface.DataType.list){
                return JSON.deserialize(data.getValue(), new TypeReference<List<HashMap>>() {
                });
            }
            return data.getValue();
//            return JSON.deserialize(data.getValue());
        }else if(data.getDataSource()==Data.DataSource.func){// 数据源为方法
            SpelService spelService = SpringContextUtil.getBeanByType(SpelService.class);
            Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
            String func = params.get("func").toString();
            Map<String,Object> paramsMap = (Map<String,Object>)params.get("params");
            return spelService.executeMethod(func,paramsMap);
        }else if(data.getDataSource()==Data.DataSource.db){   // 数据库查询
            HqlService hqlService = SpringContextUtil.getBeanByType(HqlService.class);
            Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
            String hql = params.get("hql").toString();
            String operate = params.get("operate").toString();
            return hqlService.execute(hql,operate);
        }else {
            throw new IgnoreException("不支持的转换类型");
        }
    }

}
