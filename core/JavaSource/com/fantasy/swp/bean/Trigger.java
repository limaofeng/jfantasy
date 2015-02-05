package com.fantasy.swp.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 触发器
 */
@Entity
@Table(name = "SWP_TRIGGER")
public class Trigger {

    public static enum Type {
        /**
         * 定时触发器
         */
        Scheduled,
        /**
         * 数据修改
         * 参考项：
         * 1.数据类型《classname》
         * 2.变更类型：新增、修改及删除
         * 3.变更范围：某个字段或者几个字段《 可以带条件如 attr > ?  & attr = ? 》
         */
        EntityChanged
    }

    @Id
    private Long id;

    private Type type;

    private String value;

    /*
    Schedule editor
    1.Daily
    2.Days per week
    3.Days per month
    4.Cron expression
    */
}
