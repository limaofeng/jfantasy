package com.fantasy.file.web;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileManager;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.file.service.FileService;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.web.ServletUtils;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.WebUtil.Browser;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

@Component
public class FileFilter extends GenericFilterBean {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileUploadService fileUploadService;

    private static final String regex = "_(\\d+)x(\\d+)[.]([^.]+)$";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //TODO 通过 referer 判断访问来源，并通过配置 文件管理器 与 host 关联。 String host = RegexpUtil.parseFirst(referer, "(http://|https://)[^/]+");
        final String url = request.getRequestURI().replaceAll("^" + request.getContextPath(), "");
        FileManager webrootFileManager = FileManagerFactory.getInstance().getFileManager("WEBROOT");
        if (RegexpUtil.find(url, ".do$")) {
            chain.doFilter(request, response);
            return;
        }

        if (webrootFileManager.getFileItem(url) != null) {
            chain.doFilter(request, response);
            return;
        }
        FileManager fileManager = FileManagerFactory.getInstance().getFileManager("haolue-upload");
        FileDetail fileDetail = JdbcUtil.transaction(new JdbcUtil.Callback<FileDetail>() {
            @Override
            public FileDetail run() {
                FileDetail fileDetail = FileFilter.this.fileService.getFileDetail(url, "haolue-upload");
                if (fileDetail != null) {
                    Hibernate.initialize(fileDetail);
                }
                return fileDetail;
            }
        });
        if (fileDetail != null) {
            FileItem fileItem = fileManager.getFileItem(fileDetail.getRealPath());
            if (fileItem != null) {
                writeFile(request, response, fileItem);
                return;
            }
        }
        if (RegexpUtil.find(url, regex)) {
            final String srcUrl = RegexpUtil.replace(url, regex, ".$3");
            FileDetail srcFileDetail = JdbcUtil.transaction(new JdbcUtil.Callback<FileDetail>() {
                @Override
                public FileDetail run() {
                    FileDetail fileDetail = FileFilter.this.fileService.getFileDetail(srcUrl, "haolue-upload");
                    if (fileDetail != null) {
                        Hibernate.initialize(fileDetail);
                    }
                    return fileDetail;
                }
            });
            if (srcFileDetail == null) {
                chain.doFilter(request, response);
                return;
            }
            // 查找源文件
            FileItem fileItem = fileManager.getFileItem(srcFileDetail.getRealPath());
            if (fileItem == null) {
                chain.doFilter(request, response);
                return;
            }
            // 只自动缩放 image/jpeg 格式的图片
            if (!"image/jpeg".equals(fileItem.getContentType())) {
                chain.doFilter(request, response);
                return;
            }
            RegexpUtil.Group group = RegexpUtil.parseFirstGroup(url, regex);
            // 图片缩放
            assert group != null;
            BufferedImage image = ImageUtil.reduce(fileItem.getInputStream(), Integer.valueOf(group.$(1)), Integer.valueOf(group.$(2)));
            // 创建临时文件
            File tmp = FileUtil.tmp();
            ImageUtil.write(image, tmp);
            fileDetail = fileUploadService.upload(tmp, url, "haolue-upload");
            // 删除临时文件
            FileUtil.delFile(tmp);
            writeFile(request, response, fileManager.getFileItem(fileDetail.getRealPath()));
            return;
        }
        chain.doFilter(request, response);
    }

    private void writeFile(HttpServletRequest request, HttpServletResponse response, FileItem fileItem) throws IOException {
        if ("POST".equalsIgnoreCase(WebUtil.getMethod(request))) {
            response.setContentType(fileItem.getContentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentLength((int) fileItem.getSize());

            String fileName = Browser.mozilla == WebUtil.browser(request) ? new String(fileItem.getName().getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(fileItem.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } else {
            ServletUtils.setExpiresHeader(response, 1000 * 60 * 5);
            ServletUtils.setLastModifiedHeader(response, fileItem.lastModified().getTime());
        }
        if (fileItem.getContentType().startsWith("video/")) {
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Cneonction", "close");

            String range = StringUtil.defaultValue(request.getHeader("Range"), "bytes=0-");
            if ("keep-alive".equals(request.getHeader("connection"))) {
                long fileLength = fileItem.getSize();

                response.setStatus(206);
                String bytes = WebUtil.parseQuery(range).get("bytes")[0];
                String[] sf = bytes.split("-");
                int start = 0;
                int end = 0;
                if (sf.length == 2) {
                    start = Integer.valueOf(sf[0]);
                    end = Integer.valueOf(sf[1]);
                } else if (bytes.startsWith("-")) {
                    start = 0;
                    end = (int) (fileLength - 1);
                } else if (bytes.endsWith("-")) {
                    start = Integer.valueOf(sf[0]);
                    end = (int) (fileLength - 1);
                }
                int contentLength = end - start + 1;

                response.setHeader("Connection", "keep-alive");
                response.setHeader("Content-Type", fileItem.getContentType());
                response.setHeader("Cache-Control", "max-age=1024");
                ServletUtils.setLastModifiedHeader(response, fileItem.lastModified().getTime());
                response.setHeader("Content-Length", (contentLength > fileLength ? fileLength : contentLength) + "");
                response.setHeader("Content-Range", "bytes " + start + "-" + (end != 1 && end >= fileLength ? end - 1 : end) + "/" + fileLength);

                InputStream in = fileItem.getInputStream();
                OutputStream out = response.getOutputStream();

                int loadLength = contentLength, bufferSize = 2048;

                byte[] buf = new byte[bufferSize];

                int bytesRead = in.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
                while (bytesRead != -1 && loadLength > 0) {
                    loadLength -= bytesRead;
                    out.write(buf, 0, bytesRead);
                    bytesRead = in.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
                }
                StreamUtil.closeQuietly(in);
                out.flush();
            } else {
                try {
                    StreamUtil.copy(fileItem.getInputStream(), response.getOutputStream());
                } catch (FileNotFoundException var5) {
                    logger.error(var5.getMessage(),var5);
                    response.sendError(404);
                }
            }
        } else {
            if (ServletUtils.checkIfModifiedSince(request, response, fileItem.lastModified().getTime())) {
                try {
                    StreamUtil.copy(fileItem.getInputStream(), response.getOutputStream());
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage(),e);
                    response.sendError(404);
                }
            }
        }
    }

}