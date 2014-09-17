package com.fantasy.swp.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.swp.data.SimpleData;
import com.fantasy.swp.util.DataMap;

import freemarker.template.Template;

public class PagerWriter extends AbstractWriter {

	private QueryPager queryPager;
	private List<PropertyFilter> filters = new ArrayList<PropertyFilter>();

	public void execute(Template template, DataMap data) throws IOException {
		Pager<Object> pager = queryPager.findPager(new Pager<Object>(), filters);
		do {
			data.put("pager", SimpleData.getData("pager", pager));
			OutputStream out = this.fileManager.writeFile(pageUrl.getUrl(data));
			FreeMarkerTemplateUtils.writer(data, template, out);
			pager.setCurrentPage(pager.getCurrentPage() + 1);
			pager = queryPager.findPager(pager, filters);
		} while (pager.getCurrentPage() <= pager.getTotalPage());
	}

	public static interface QueryPager {

		public Pager<Object> findPager(Pager<Object> pager, List<PropertyFilter> filters);

	}

}
