package org.jfantasy.system.dao;

import org.jfantasy.system.bean.ChartSource;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.system.bean.WebAccessLog;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WebAccessLogDao extends HibernateDao<WebAccessLog, Long> {

    public List<ChartSource> queryBrowser(){
        List<ChartSource> chartSourceList = new ArrayList<ChartSource>();
        SQLQuery sqlQuery = this.createSQLQuery("SELECT COUNT(*) AS num,browser AS browser FROM sys_web_access_log  GROUP BY browser");
        for(int i=0;i<sqlQuery.list().size();i++){
            ChartSource source = new ChartSource();
            Object[] objects =(Object[])sqlQuery.list().get(i);
            source.setValue(objects[0].toString());
            source.setKey(objects[1].toString());
            source.setDescription("");
            chartSourceList.add(source);
        }
        return chartSourceList;
    }

    public List<ChartSource> queryNum(){
        List<ChartSource> chartSourceList = new ArrayList<ChartSource>();
        SQLQuery sqlQuery = this.createSQLQuery("SELECT COUNT(creator) AS num,DATE_FORMAT(create_time,'%Y-%m-%d') AS time FROM sys_web_access_log GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d')");
        for(int i=0;i<sqlQuery.list().size();i++){
            ChartSource source = new ChartSource();
            Object[] objects =(Object[])sqlQuery.list().get(i);
            source.setValue(objects[0].toString());
            source.setKey(objects[1].toString());
            source.setDescription("");
            chartSourceList.add(source);
        }
        return chartSourceList;
    }

    public List<ChartSource> queryIpClickNum(){
        List<ChartSource> chartSourceList = new ArrayList<ChartSource>();
        SQLQuery sqlQuery = this.createSQLQuery("SELECT COUNT(a),a FROM (SELECT COUNT(user_ip),DATE_FORMAT(create_time,'%Y-%m-%d') AS a,user_ip FROM sys_web_access_log  GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d'),user_ip)t GROUP BY a;");
        for(int i=0;i<sqlQuery.list().size();i++){
            ChartSource source = new ChartSource();
            Object[] objects =(Object[])sqlQuery.list().get(i);
            source.setValue(objects[0].toString());
            source.setKey(objects[1].toString());
            source.setDescription("");
            chartSourceList.add(source);
        }
        return chartSourceList;
    }

}
