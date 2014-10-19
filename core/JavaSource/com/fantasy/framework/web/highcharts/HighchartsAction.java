//package com.fantasy.framework.web.highcharts;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringReader;
//import java.net.URLEncoder;
//
//import org.apache.batik.transcoder.Transcoder;
//import org.apache.batik.transcoder.TranscoderException;
//import org.apache.batik.transcoder.TranscoderInput;
//import org.apache.batik.transcoder.TranscoderOutput;
//import org.apache.batik.transcoder.image.JPEGTranscoder;
//import org.apache.batik.transcoder.image.PNGTranscoder;
//import org.apache.fop.svg.PDFTranscoder;
//
//import com.fantasy.framework.struts2.ActionSupport;
//import com.fantasy.framework.util.web.WebUtil;
//
//public class HighchartsAction extends ActionSupport {
//	private static final long serialVersionUID = 1211591418608430948L;
//	private String type;
//	private String svg;
//	private String filename;
//	public InputStream inputStream;
//	public String contentType;
//
//	public String export() throws IOException, TranscoderException {
//		this.contentType = this.type;
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		if ((this.type != null) && (this.svg != null)) {
//			this.svg = this.svg.replaceAll(":rect", "rect");
//			String ext = "";
//			Transcoder t = null;
//			if (this.type.equals("image/png")) {
//				ext = "png";
//				t = new PNGTranscoder();
//			} else if (this.type.equals("image/jpeg")) {
//				ext = "jpg";
//				t = new JPEGTranscoder();
//			} else if (this.type.equals("application/pdf")) {
//				ext = "pdf";
//				t = new PDFTranscoder();
//			} else if (this.type.equals("image/svg+xml")) {
//				this.filename = ((WebUtil.Browser.mozilla == WebUtil.browser(this.request) ? new String(this.filename.getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(this.filename, "UTF-8")) + ".svg");
//				this.inputStream = new ByteArrayInputStream(this.svg.getBytes());
//			}
//			if (t != null) {
//				TranscoderInput input = new TranscoderInput(new StringReader(this.svg));
//				TranscoderOutput output = new TranscoderOutput(out);
//				t.transcode(input, output);
//				this.filename = ((WebUtil.Browser.mozilla == WebUtil.browser(this.request) ? new String(this.filename.getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(this.filename, "UTF-8")) + "." + ext);
//				this.inputStream = new ByteArrayInputStream(out.toByteArray());
//			}
//		}
//		return "success";
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public void setSvg(String svg) {
//		this.svg = svg;
//	}
//
//	public void setFilename(String filename) {
//		this.filename = filename;
//	}
//
//	public String getFilename() {
//		return this.filename;
//	}
//
//	public InputStream getInputStream() {
//		return this.inputStream;
//	}
//
//	public String getContentType() {
//		return this.contentType;
//	}
//}