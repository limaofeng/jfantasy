package com.fantasy.swp.writer;

import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.swp.OutPutUrl;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.TemplateData;
import com.fantasy.swp.util.DataMap;
import freemarker.template.Template;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleWriter extends AbstractWriter {

	public void execute(Template template, DataMap data) throws IOException {
		OutputStream out = this.fileManager.writeFile(pageUrl.getUrl(data));
		FreeMarkerTemplateUtils.writer(data, template, out);
	}

	@Override
	public PageInstance createPageInstance(OutPutUrl outPutUrl, com.fantasy.swp.Template template, TemplateData... datas) {
		return null;
	}
}
