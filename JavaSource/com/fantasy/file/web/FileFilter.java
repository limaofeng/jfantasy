package com.fantasy.file.web;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.web.ServletUtils;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.WebUtil.Browser;
import com.fantasy.system.util.SettingUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component("fileFilter")
public class FileFilter extends GenericFilterBean {

	private final static ConcurrentLinkedQueue<String> noInFileManagerCache = new ConcurrentLinkedQueue<String>();
	private final static ConcurrentMap<String, FileItem> fileCache = new ConcurrentHashMap<String, FileItem>();

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
	}

	private static final String regex = "_(\\d+)x(\\d+)[.]([^.]+)$";

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = request.getRequestURI().replaceAll("^" + request.getContextPath(), "");
		if (RegexpUtil.find(url, ".do$") || noInFileManagerCache.contains(url)) {
			chain.doFilter(request, response);
			return;
		}
		if (FileManagerFactory.getInstance().getFileManager("WEBROOT").getFileItem(url) != null) {
			noInFileManagerCache.add(url);
			chain.doFilter(request, response);
			return;
		}
		if (fileCache.containsKey(url)) {
			writeFile(request, response, fileCache.get(url));
			return;
		}
		FileManager fileManager = SettingUtil.getDefaultUploadFileManager();
		if (fileManager != null) {
			FileItem fileItem = fileManager.getFileItem(url);
			if (fileItem == null && RegexpUtil.find(url, regex)) {
				// 查找源文件
				String srcUrl = RegexpUtil.replace(url, regex, ".$3");
				if (fileCache.containsKey(srcUrl)) {
					fileItem = fileCache.get(srcUrl);
				} else {
					fileItem = fileManager.getFileItem(srcUrl);
					if (fileItem == null) {
//						noInFileManagerCache.add(url);
						chain.doFilter(request, response);
						return;
					}
					// 缓存原始路径对应的文件
					if (!fileCache.containsKey(srcUrl)) {
						fileCache.put(srcUrl, fileItem);
					}
					// 只自动缩放 image/jpeg 格式的图片
					if (!"image/jpeg".equals(fileItem.getContentType())) {
//						noInFileManagerCache.add(url);
						chain.doFilter(request, response);
						return;
					}
				}
				RegexpUtil.Group group = RegexpUtil.parseFirstGroup(url, regex);
				// 图片缩放
				BufferedImage image = ImageUtil.reduce(fileItem.getInputStream(), Integer.valueOf(group.$(1)), Integer.valueOf(group.$(2)));
				// 创建临时文件
				File tmp = FileUtil.tmp();
				ImageUtil.write(image, tmp);
				fileManager.writeFile(url, tmp);
				// 删除临时文件
				FileUtil.delFile(tmp);
				fileCache.put(url, fileItem = fileManager.getFileItem(url));
				writeFile(request, response, fileItem);
				return;
			}
			if (fileItem != null) {
				fileCache.put(url, fileItem);
				writeFile(request, response, fileItem);
				return;
			}
		}
		noInFileManagerCache.add(url);
		chain.doFilter(request, response);
		
	}

	public static void addFileCache(String url, FileItem fileItem) {
		fileCache.put(url, fileItem);
	}

	public static FileItem getFileCache(String url) {
		return fileCache.get(url);
	}

	private void writeFile(HttpServletRequest request, HttpServletResponse response, FileItem fileItem) throws IOException {
		if ("POST".equalsIgnoreCase(WebUtil.getMethod(request))) {
			response.setContentType(fileItem.getContentType());
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentLength((int) fileItem.getSize());

			String fileName = (Browser.mozilla == WebUtil.browser(request) ? new String(fileItem.getName().getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(fileItem.getName(), "UTF-8"));
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		} else {
			ServletUtils.setExpiresHeader(response, 1000 * 60 * 5);
			ServletUtils.setLastModifiedHeader(response, fileItem.lastModified().getTime());
		}
		if (ServletUtils.checkIfModifiedSince(request, response, fileItem.lastModified().getTime())) {
			try{
				StreamUtil.copy(fileItem.getInputStream(), response.getOutputStream());
			}catch (FileNotFoundException e) {
				response.sendError(404);
			}
		}
	}

}