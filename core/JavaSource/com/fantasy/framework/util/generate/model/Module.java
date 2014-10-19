package com.fantasy.framework.util.generate.model;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String classPath;
    private List<Table> tables = new ArrayList<Table>();
    private String beansPackage;
    private String webPackage;
    private String daoPackage;
    private String servicePackage;

    public Module(String classPath) {
	this.classPath = classPath;
    }

    public String getSqlMapFilePath() {
	return null;
    }

    public void addTable(Table table) {
	this.tables.add(table);
    }

    public String getClassPath() {
	return this.classPath;
    }

    public void setClassPath(String classPath) {
	this.classPath = classPath;
    }

    public List<Table> getTables() {
	return this.tables;
    }

    public void setTables(List<Table> tables) {
	this.tables = tables;
    }

    public String getBeansPackage() {
	return this.beansPackage;
    }

    public void setBeansPackage(String beansPackage) {
	this.beansPackage = beansPackage;
    }

    public String getWebPackage() {
	return this.webPackage;
    }

    public void setWebPackage(String webPackage) {
	this.webPackage = webPackage;
    }

    public String getDaoPackage() {
	return this.daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
	this.daoPackage = daoPackage;
    }

    public String getServicePackage() {
	return this.servicePackage;
    }

    public void setServicePackage(String servicePackage) {
	this.servicePackage = servicePackage;
    }
}