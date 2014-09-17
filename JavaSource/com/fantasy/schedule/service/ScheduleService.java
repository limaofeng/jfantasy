package com.fantasy.schedule.service;

/**
 * Created by yhx on 2014/8/12.
 */
public class ScheduleService {


    /**
     * 返回 各时段的表达式
     * @param cron 表达式
     * @param i 下标
     * @return
     */
    public static String cron(String cron,int i){
        String str="";
        if("".equals(cron)||cron==null) return str;
        String[] crons = cron.split(" ");
        for(int a=0;a<crons.length;a++){
            if(i==a){
                str=crons[a];
                break;
            }

        }
        return  str;
    }

}
