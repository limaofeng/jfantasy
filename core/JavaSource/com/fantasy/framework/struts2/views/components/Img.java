package com.fantasy.framework.struts2.views.components;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.system.util.SettingUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@StrutsTag(name = "img", tldTagClass = "com.fantasy.framework.struts2.views.jsp.ui.ImgTag", description = "", allowDynamicAttributes = true)
public class Img extends UIBean {

	private static final Log logger = LogFactory.getLog(Img.class);

	private static final String TEMPLATE = "img";
	private String src;
	private String alt;
	private String ratio;

	public Img(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		String tmpsrc = this.response.encodeURL(ensureAttributeSafelyNotEscaped(findString(this.src)));
		if (ratio != null) {
			tmpsrc = RegexpUtil.replace(tmpsrc, "[.][^.]{1,}$", "_" + this.ratio + "$0");
            addParameter("ratio", this.ratio);
            String[] tmpRatio = RegexpUtil.split(ratio, "x");
            addParameter("cssStyle", StringUtil.defaultValue(this.cssStyle,"").concat("width:"+tmpRatio[0]+"px;height:"+tmpRatio[1]+"px"));
		}
		addParameter("src", SettingUtil.get("website", "img-contextPath", this.request.getContextPath()) + tmpsrc);

		if (alt != null) {
			addParameter("alt", findString(alt));
		}
		/*
		if (ratio != null) {
			FileItem fileItem = FileFilter.getFileCache(tmpsrc);
			if (fileItem != null) {
				return;
			}
			FileManager fileManager = SettingUtil.getDefaultUploadFileManager();
			fileItem = fileManager.getFileItem(tmpsrc);
			if (fileItem != null) {
				FileFilter.addFileCache(tmpsrc, fileItem);
				return;
			}
			fileItem = FileFilter.getFileCache(src);
			if (fileItem == null) {
				fileItem = fileManager.getFileItem(src);
				if (fileItem == null) {
					return;
				}
				FileFilter.addFileCache(src, fileItem);
			}
			// 只自动缩放 image/jpeg 格式的图片
			if (!"image/jpeg".equals(fileItem.getContentType())) {
				return;
			}
			try {
				String[] tmpRatio = RegexpUtil.split(ratio, "x");
				// 图片缩放
				BufferedImage image = ImageUtil.reduce(fileItem.getInputStream(), Integer.valueOf(tmpRatio[0]), Integer.valueOf(tmpRatio[1]));
				// 创建临时文件
				File tmp = FileUtil.tmp();
				ImageUtil.write(image, tmp);
				fileManager.writeFile(tmpsrc, tmp);
				// 删除临时文件
				FileUtil.delFile(tmp);
				FileFilter.addFileCache(tmpsrc, fileItem = fileManager.getFileItem(tmpsrc));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}*/
	}

	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	@StrutsTagAttribute(description = " HTML for attribute", type = "String")
	public void setSrc(String src) {
		this.src = src;
	}

	@StrutsTagAttribute(description = " HTML for attribute", type = "String")
	public void setAlt(String alt) {
		this.alt = alt;
	}

	@StrutsTagAttribute(description = " HTML for attribute", type = "String")
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}