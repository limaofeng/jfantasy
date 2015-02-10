package com.fantasy.swp.listener;


import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpELUtil;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.swp.bean.PageItemData;
import com.fantasy.swp.bean.Trigger;
import com.fantasy.swp.service.PageItemDataService;
import com.fantasy.swp.service.TriggerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityChangedEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private static final Log LOG = LogFactory.getLog(EntityChangedEventListener.class);

    @Override
    public void onPostDelete(PostDeleteEvent event) {

    }

    @Override
    public void onPostInsert(PostInsertEvent event) {

    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        EntityPersister entityPersister = event.getPersister();
        Class<?> clazz = ClassUtil.forName(entityPersister.getRootEntityName());
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_beanId",event.getId().toString()));
        filters.add(new PropertyFilter("EQS_className",clazz.getName()));
        PageItemDataService pageItemDataService = SpringContextUtil.getBeanByType(PageItemDataService.class);
        List<PageItemData> pageItemDataList = pageItemDataService.find(filters);
        if(pageItemDataList.size()>0){
            Long pageId = pageItemDataList.get(0).getPageItem().getPage().getId();
            TriggerService triggerService =  SpringContextUtil.getBeanByType(TriggerService.class);
            List<PropertyFilter> triggerFilter = new ArrayList<PropertyFilter>();
            triggerFilter.add(new PropertyFilter("EQL_page.id",pageId.toString()));
            List<Trigger> triggerList = triggerService.find(triggerFilter);
            String[][] attr = new String[triggerList.size()][];
            for(int i = 0;i<triggerList.size();i++){
                EvaluationContext context = SpELUtil.createEvaluationContext(event.getEntity());
                Expression expression = SpELUtil.getExpression(triggerList.get(i).getPriorityCondition() == null ? "true" : triggerList.get(i).getPriorityCondition());
                Boolean retVal = expression.getValue(context, Boolean.class);
                if (retVal && "EntityChanged".equals(triggerList.get(i).getType().toString()) && triggerList.get(i).getValue().contains(",")) {
                    attr[i] = triggerList.get(i).getValue().split(",");
                }
            }
            for(String[] i :attr){
               if(i!=null){
                   for(String j:i){
                       if(!this.modify(event,j)){
                           for(PageItemData itemData:pageItemDataList){
                               LOG.debug("重新生成的pageItem的id为===="+itemData.getPageItem().getId());
                           }
                       }
                   }
               }
            }
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    public boolean modify(PostUpdateEvent event,String property) {
        Arrays.binarySearch(event.getPersister().getPropertyNames(), property);
        int index = ObjectUtil.indexOf(event.getPersister().getPropertyNames(), property);
        return index != -1 && event.getState()[index].equals(event.getOldState()[index]);
    }

}
