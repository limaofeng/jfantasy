package com.fantasy.framework.freemarker.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

import com.fantasy.framework.service.FTPService;

import freemarker.cache.TemplateLoader;

public class FTPTemplateLoader implements TemplateLoader{
	
	private FTPService ftpService;

	public void closeTemplateSource(Object templateSource) throws IOException {
	}

	public Object findTemplateSource(String name) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ftpService.download(name, out);
		return out.toString();
	}

	public long getLastModified(Object arg0) {
		return 0;
//		return ftpService.getLastModified();
	}

	public Reader getReader(Object templateSource, final String encoding) throws IOException {
		return null;
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ftpService.download(name, out);
//		System.out.println(out.toString());
//		return out.toString();
	}

	public void setFtpService(FTPService ftpService) {
		this.ftpService = ftpService;
	}

}
