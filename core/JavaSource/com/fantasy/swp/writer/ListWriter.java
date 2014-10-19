package com.fantasy.swp.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.swp.data.SimpleData;
import com.fantasy.swp.util.DataMap;

import freemarker.template.Template;

public class ListWriter extends AbstractWriter {

	private List<Object> list;

	public void execute(Template template, DataMap data) throws IOException {
		if (list == null) {
			throw new RuntimeException("list is null");
		}
		for (Object object : list) {
			data.put("item", SimpleData.getData("item", object));
			OutputStream out = this.fileManager.writeFile(pageUrl.getUrl(data));
			FreeMarkerTemplateUtils.writer(data, template, out);
		}
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

}
