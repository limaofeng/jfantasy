package com.fantasy.framework.util.generate;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.generate.model.Column;
import com.fantasy.framework.util.generate.model.Module;
import com.fantasy.framework.util.generate.model.PageSql;
import com.fantasy.framework.util.generate.model.Table;
import com.fantasy.framework.util.xml.JdomUtil;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/fantasy/framework/util/generate/applicationContext-gc.xml" })
public class GeneraterUtil {

    @Resource
    private ColumnMapper tabColumnMapper;

    @Before
    public void init() {
	JdomUtil.parse(this.getClass().getResourceAsStream("dataDictGenerate.xml"), parser);
    }

    private HashMap<String, String> dataTypes = new HashMap<String, String>();

    private HashMap<String, Module> modules = new HashMap<String, Module>();

    /**
     * 解析dataDictGenerate.xml文件
     */
    private JdomUtil.Parser parser = new JdomUtil.Parser() {

	public void callBack(String tagerName, Element ele) {
	    if ("dataType".equals(tagerName)) {
		dataTypes.put(ele.getAttributeValue("baseType"), ele.getAttributeValue("javaType"));
	    } else if ("module".equals(tagerName)) {
		Module module = new Module(ele.getAttributeValue("class"));
		for (Element tab_ele : (List<Element>) ele.getChildren()) {
		    String tableName = tab_ele.getAttributeValue("name");
		    String className = tab_ele.getAttributeValue("class");
		    String id = tab_ele.getAttributeValue("id");
		    Table table = new Table(tableName, module.getClassPath().concat(".").concat("[sign]").concat(".").concat(className));
		    table.setColumns(tabColumnMapper.getTabColumnsByTabName(table.getTableName()));
		    table.setComments(tabColumnMapper.getTabCommentsByTabName(table.getTableName()));
		    if (StringUtil.isBlank(id))
			return;
		    for (Column column : table.getColumns()) {
			if (column.getColumnName().equalsIgnoreCase(id)) {
			    table.setPrimaryKey(column);
			}
		    }
		    for (Element sql_ele : (List<Element>) tab_ele.getChildren()) {
			PageSql pageSql = new PageSql(sql_ele.getAttributeValue("id"), sql_ele.getTextTrim());
			table.addPageSql(pageSql);
		    }
		    module.addTable(table);
		}
		modules.put(module.getClassPath(), module);
	    }
	}
    };

    @Test
    public void generate() throws Exception {
	// this.dataTypes
//	FreemarkerService freemarkerService = new FreemarkerService();
//	freemarkerService.addUtil("util", new TemplateUtil(dataTypes));
//	freemarkerService.addUtil("RegexpUtil", "aia.cn.isp.core.util.regexp.RegexpUtil");
//	for (Entry<String, Module> entry : modules.entrySet()) {
//	    new MyBatisBinder(freemarkerService).generate(new File("").getCanonicalFile().getPath().concat("/core"), entry.getKey(), entry.getValue());
//	}
    }

}
