package com.fantasy.framework.web.validator.action;

import com.fantasy.framework.struts2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

@Deprecated
public class ImageUtilAction extends ActionSupport {
	private static final long serialVersionUID = 8746175169525664411L;
	protected static Logger logger = LoggerFactory.getLogger(ImageUtilAction.class);

	public String execute() throws Exception {
		return "success";
	}

	public String photoShot(String picture,String toPicture,int y,int x,int w,int h,float ratio) throws Exception {
//		ImageUtil.write(ImageUtil.screenshots(PathUtil.getFilePath(picture), x, y, w, h, ratio), PathUtil.getFilePath(toPicture));
		return "success";
	}

}