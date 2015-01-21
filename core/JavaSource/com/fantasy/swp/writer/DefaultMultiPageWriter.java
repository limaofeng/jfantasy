package com.fantasy.swp.writer;

import com.fantasy.swp.OutPutUrl;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.TemplateData;
import com.fantasy.swp.util.DataMap;
import freemarker.template.Template;

import java.io.IOException;

public class DefaultMultiPageWriter extends AbstractWriter {

	public void execute(Template template, DataMap data) throws IOException {
		
	}

	@Override
	public PageInstance createPageInstance(OutPutUrl outPutUrl, com.fantasy.swp.Template template, TemplateData... datas) {
		return null;
	}
}
