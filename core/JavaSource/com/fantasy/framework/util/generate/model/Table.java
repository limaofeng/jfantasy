package com.fantasy.framework.util.generate.model;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableName;
    private String className;
    private String comments;
    private List<PageSql> pageSqls = new ArrayList<PageSql>();
    private Column primaryKey;
    public List<Column> columns = new ArrayList<Column>();

    public Column getPrimaryKey() {
        return this.primaryKey;
    }

    public List<Column> listColumn() {
        return this.columns;
    }

    public Table(String tableName, String className) {
        this.tableName = tableName;
        this.className = className;
    }

    public void addPageSql(PageSql pageSql) {
        this.pageSqls.add(pageSql);
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getComments() {
        return StringUtil.nullValue(this.comments);
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<PageSql> getPageSqls() {
        return this.pageSqls;
    }

    public void setPageSqls(List<PageSql> pageSqls) {
        this.pageSqls = pageSqls;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getSimpleName() {
        return RegexpUtil.parseFirst(this.className, "[a-zA-Z0-9_]+$");
    }

    public String getFilePath(String fileName) {
        return getFilePath(fileName, "");
    }

    public String getFilePath(String fileName, String suffix) {
        return RegexpUtil.replace(RegexpUtil.replace(this.className, "\\[sign\\]", fileName), "\\.", "/").concat(suffix);
    }

    public String getPackage(String name) {
        return RegexpUtil.replace(RegexpUtil.replace(this.className, "\\[sign\\]", name), ".[a-zA-Z0-9_]+$", "");
    }
}