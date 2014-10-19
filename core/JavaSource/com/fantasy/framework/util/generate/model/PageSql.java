package com.fantasy.framework.util.generate.model;

public class PageSql {
    private String id;
    private String sql;

    public PageSql(String id, String sql) {
	this.id = id;
	this.sql = sql;
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getSql() {
	return this.sql;
    }

    public void setSql(String sql) {
	this.sql = sql;
    }
}