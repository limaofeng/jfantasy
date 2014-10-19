package com.fantasy.framework.util.generate.model;

import org.apache.ibatis.type.Alias;

@Alias("Column")
public class Column {
    private String columnName;
    private String dataType;
    private int dataLength;
    private String nullable;
    private String comments;
    private String dataDefault;
    private boolean isNull;

    public String getColumnName() {
	return this.columnName;
    }

    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    public String getDataType() {
	return this.dataType;
    }

    public void setDataType(String dataType) {
	this.dataType = dataType;
    }

    public int getDataLength() {
	return this.dataLength;
    }

    public void setDataLength(int dataLength) {
	this.dataLength = dataLength;
    }

    public String getNullable() {
	return this.nullable;
    }

    public void setNullable(String nullable) {
	this.nullable = nullable;
    }

    public String getComments() {
	return this.comments == null ? "" : this.comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public String getDataDefault() {
	return this.dataDefault;
    }

    public void setDataDefault(String dataDefault) {
	this.dataDefault = dataDefault;
    }

    public boolean isNull() {
	return this.isNull;
    }

    public void setNull(boolean isNull) {
	this.isNull = isNull;
    }
}