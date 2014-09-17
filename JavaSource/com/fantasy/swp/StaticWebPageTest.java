package com.fantasy.swp;

import java.io.IOException;

import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.swp.url.ExpressionUrl;
import com.fantasy.swp.util.DataMap;
import com.fantasy.swp.writer.SimpleWriter;

public class StaticWebPageTest {
	
	public static void main(String[] args) throws IOException {
		SimpleWriter writer = new SimpleWriter();
		writer.setFileManager(new LocalFileManager());
		writer.setPageUrl(new ExpressionUrl(""));
		
//		writer.execute(null, new DataMap());
	}
}
