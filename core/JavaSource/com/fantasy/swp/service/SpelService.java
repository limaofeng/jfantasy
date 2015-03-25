package com.fantasy.swp.service;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpELUtil;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.security.service.UserService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行方法
 * Created by wml on 2015/1/28.
 */
@Service
@Transactional
public class SpelService {

    private static final Map<String,Object> servers = new HashMap<String, Object>();

    public static void setServer(String key, Object service){
        if(servers.get(key)!=null){
            return;
        }
        servers.put(key, service);
    }

    public static Object getServer(String key){
        if(servers.isEmpty()){
            init();
        }
        return servers.get(key);
    }

    public static Map<String,Object> getAllServer(){
        if(servers.isEmpty()){
            init();
        }
        return servers;
    }

    private static void init(){
        servers.put("userService",SpringContextUtil.getBeanByType(UserService.class));
    }

    public Object executeMethod(String mothod,Map<String,Object> data){
        EvaluationContext context = SpELUtil.createEvaluationContext(this.getAllServer());
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if(data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if ("filters".equals(entry.getKey())) {    // List<PropertyFilter>条件
                    List<Map<String, String>> _params = (List<Map<String, String>>) entry.getValue();
                    for (int i = 0; i < _params.size(); i++) {
                        PropertyFilter filter = new PropertyFilter(_params.get(i).get("key"), _params.get(i).get("value"));
                        filters.add(filter);
                    }
                    context.setVariable("filters", filters);
                } else if ("stat".equals(entry.getKey())) {  // 静态
                    Map<String, Object> _v = (Map<String, Object>) entry.getValue();
                    for (Map.Entry<String, Object> _entry : _v.entrySet()) {
                        context.setVariable(_entry.getKey(), _entry.getValue());
                    }
                }
            }
        }
        Expression expression = SpELUtil.getExpression(mothod);
        Object object = expression.getValue(context);
        return object;

    }
}
