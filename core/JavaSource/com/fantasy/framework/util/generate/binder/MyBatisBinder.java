package com.fantasy.framework.util.generate.binder;


import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.generate.model.Column;
import com.fantasy.framework.util.generate.model.Module;
import com.fantasy.framework.util.generate.model.Table;
import com.fantasy.framework.util.generate.util.TemplateUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MyBatisBinder {

    private FreemarkerService freemarkerService;

    private Map<String, String> packages = new HashMap<String, String>();
    {
	packages.put("bean", "domain");
	packages.put("dao", "dao");
	packages.put("service", "service");
	packages.put("serviceImpl", "service.impl");
	packages.put("mapper", "mapper");
	packages.put("sqlmap", "mapper");
	packages.put("controller", "controller");
	packages.put("form", "controller.form");
	packages.put("test", "test");
    }

    public MyBatisBinder(FreemarkerService freemarkerService) {
	this.freemarkerService = freemarkerService;
    }

    public void generate(String javasource, String path, Module module){
        try{
            for (Table table : module.getTables()) {
                this.generateJavaBean(javasource, table);
                this.generateJavaService(javasource, table);
                this.generateJavaServiceImpl(javasource, table);
                this.generateJavaDao(javasource, table);
                this.generateJavaMapper(javasource, table);
                this.generateSqlMap(javasource, table);
            }
        }catch (Exception e){
            throw new IgnoreException(e.getMessage());
        }

    }

    public void generateFormBean(String javasource, Table table)  {
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("form"), "Form.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    Assert.notNull(javaClass, tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("package", table.getPackage(packages.get("form")));
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "form.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    public void generateController(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("controller"), "Controller.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map data = new HashMap();
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("formBeanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("form")) + "Form");
	data.put("package", table.getPackage(packages.get("controller")));
	data.put("serviceClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("service")).concat("Service"));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "controller.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    @SuppressWarnings("unused")
    private void generateTest(String javasource, Table table) {
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("test"), "ServiceTest.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map data = new HashMap();
	data.put("package", table.getPackage(packages.get("test")));
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("serviceClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("service")).concat("Service"));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "test.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    public void generateSqlMap(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("mapper"), "-Mapper.xml")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map data = new HashMap();
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("mapperClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("mapper")).concat("Mapper"));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "sqlmap.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    public void generateJavaMapper(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("mapper"), "Mapper.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("package", table.getPackage(packages.get("mapper")));
	data.put("IMapper", SqlMapper.class.getName());
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "mapper.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    public void generateJavaDao(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("dao"), "Dao.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("mapperClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("mapper")).concat("Mapper"));
	data.put("package", table.getPackage(packages.get("dao")));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "dao.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    private void generateJavaServiceImpl(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("serviceImpl"), "ServiceImpl.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("package", table.getPackage(packages.get("serviceImpl")));
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("daoClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("dao")).concat("Dao"));
	data.put("serviceClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("service")).concat("Service"));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "serviceImpl.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    public void generateJavaBean(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("bean"), ".java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    Assert.notNull(javaClass, tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("package", table.getPackage(packages.get("bean")));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "bean.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

    private void generateJavaService(String javasource, Table table){
	File file = FileUtil.createFile(javasource.concat("/").concat(table.getFilePath(packages.get("service"), "Service.java")));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new IgnoreException(e.getMessage());
        }
        Map<String, String> importsMap = new HashMap<String, String>();
	TemplateUtil util = (TemplateUtil) ((Map<String, Object>) ClassUtil.getValue(freemarkerService, "utils")).get("util");
	for (Column tabColumn : table.listColumn()) {
	    String javaClass = util.toJavaType(tabColumn.getDataType());
	    if (!javaClass.startsWith("java.lang.")) {
		importsMap.put(javaClass, javaClass);
	    }
	}
	String[] imports = importsMap.keySet().toArray(new String[0]);
	Map data = new HashMap();
	data.put("imports", imports);
	data.put("package", table.getPackage(packages.get("service")));
	data.put("beanClass", RegexpUtil.replace(table.getClassName(), "\\[sign\\]", packages.get("bean")));
	data.put("table", table);
	try {
	    freemarkerService.writer(data, MyBatisBinder.class, "template", "service.ftl", out);
	} finally {
	    IOUtils.closeQuietly(out);
	}
    }

}