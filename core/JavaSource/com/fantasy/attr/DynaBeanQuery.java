package com.fantasy.attr;

import com.fantasy.framework.dao.hibernate.util.TypeFactory;
import com.fantasy.framework.util.common.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态bean，查询扩展
 */
public class DynaBeanQuery {

    private final static Log logger = LogFactory.getLog(DynaBeanQuery.class);

    private List<Column> columns = new ArrayList<Column>();
    private List<Column> subColumns = new ArrayList<Column>();
    private boolean dynamicQuery = false;

    public static DynaBeanQuery createDynaBeanQuery() {
        return new DynaBeanQuery();
    }

    public void addColumn(String name, Class<?> type) {
        if (!this.dynamicQuery) {
            this.dynamicQuery = true;
        }
        this.columns.add(new Column(name, type));
    }

    public void addColumn(String name, Class<?> type, String fkName) {
        if (!this.dynamicQuery) {
            this.dynamicQuery = true;
        }
        Column column = new Column(name, type, fkName);
        this.columns.add(column);
        this.subColumns.add(column);
    }

    public boolean isDynamicQuery() {
        return this.dynamicQuery;
    }

    public String[] toColumns(String alias, String propertyName) {
        logger.debug("alias:" + alias + ",propertyName:" + propertyName);
        return new String[]{"attribute1_" + ".value"};
    }

    public String[] toColumns(String alias, int propertyIndex) {
        logger.debug("alias:" + alias + ",propertyIndex:" + propertyIndex);
        return new String[]{"attribute1_" + ".value"};
    }

    public boolean inColumn(String propertyName) {
        return ObjectUtil.find(columns, "name", propertyName) != null;
    }

    public Type toType(String propertyName, SessionFactoryImplementor factory) {
        Column column = ObjectUtil.find(subColumns, "name", propertyName);
        if (column != null) {
            return factory.getTypeResolver().getTypeFactory().manyToOne(column.getType().getName(), false, column.getFkName(), false, false, false, false);
        }
        column = ObjectUtil.find(columns, "name", propertyName);
        return TypeFactory.basic(column.getType().getName());
    }

    public int countSubclassProperties() {
        return subColumns.size();
    }

    public Type getSubclassPropertyType(int i, SessionFactoryImplementor factory) {
        return toType(getSubclassPropertyName(i), factory);
    }

    public String getSubclassPropertyName(int i) {
        return subColumns.get(i).getName();
    }

    public String[] getSubclassPropertyColumnNames(int i) {
        return new String[]{subColumns.get(i).getName()};
    }

    public static class Column {
        private String name;
        private Class<?> type;
        private String fkName;

        public Column(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public Column(String name, Class<?> type, String fkName) {
            this.name = name;
            this.type = type;
            this.fkName = fkName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public String getFkName() {
            return fkName;
        }

        public void setFkName(String fkName) {
            this.fkName = fkName;
        }

    }

}
