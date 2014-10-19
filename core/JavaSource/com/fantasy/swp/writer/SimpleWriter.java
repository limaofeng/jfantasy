package com.fantasy.swp.writer;

import java.io.IOException;
import java.io.OutputStream;

import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.swp.util.DataMap;

import freemarker.template.Template;

public class SimpleWriter extends AbstractWriter {

	public void execute(Template template, DataMap data) throws IOException {
		OutputStream out = this.fileManager.writeFile(pageUrl.getUrl(data));
		FreeMarkerTemplateUtils.writer(data, template, out);
	}

}
