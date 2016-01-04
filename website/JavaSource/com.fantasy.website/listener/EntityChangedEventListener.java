package org.jfantasy.website.listener;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.SpELUtil;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.schedule.service.ScheduleService;
import org.jfantasy.website.bean.PageItemData;
import org.jfantasy.website.bean.Trigger;
import org.jfantasy.website.schedule.CreateHtmlJob;
import org.jfantasy.website.service.PageItemDataService;
import org.jfantasy.website.service.TriggerService;
import org.quartz.JobKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

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
                if("EntityChanged".equals(triggerList.get(i).getType().toString())){
                    EvaluationContext context = SpELUtil.createEvaluationContext(event.getEntity());
                    Expression expression = SpELUtil.getExpression(triggerList.get(i).getPriorityCondition() == null ? "true" : triggerList.get(i).getPriorityCondition());
                    Boolean retVal = expression.getValue(context, Boolean.class);
                    if (retVal && triggerList.get(i).getValue().contains(",")) {
                        attr[i] = triggerList.get(i).getValue().split(",");
                    }
                }
            }
            Map<String, String> data = new HashMap<String, String>();
            for(String[] i :attr){
               if(i!=null){
                   for(String j:i){
                       if(!this.modify(event,j)){
                           for(int k =0;k<pageItemDataList.size();k++){
                                PageItemData itemData = pageItemDataList.get(k);
                                data.put(String.valueOf(k),itemData.getId().toString());
                           }
                       }
                   }
               }
            }
            if(data.size()>0){
                ScheduleService scheduleService = SpringContextUtil.getBeanByType(ScheduleService.class);
                // 添加 job
                scheduleService.addJob(JobKey.jobKey("pageItemCreate", "html"), CreateHtmlJob.class,data);
                //定义触发时间   5秒之后触发
                //Date date = DateUtil.now();
                //String expression = DateUtil.format(DateUtil.add(date, Calendar.SECOND,5),"ss mm HH dd MM ? yyyy");
                // 立刻触发
                scheduleService.triggerJob(JobKey.jobKey("pageItemCreate", "html"),data);
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
