package org.jfantasy.framework.util.common.file.gzip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZipResponse extends HttpServletResponseWrapper {
	
	private GZipStream stream;
	private PrintWriter writer;

	public GZipResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.stream = new GZipStream(response.getOutputStream());
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return this.stream;
	}

	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
			this.writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
		}
		return this.writer;
	}

	public void flush() throws IOException {
		if (this.writer != null) {
			this.writer.flush();
		}
		this.stream.finish();
	}
}