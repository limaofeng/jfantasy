package com.fantasy.file.web;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.framework.util.common.PathUtil;
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
import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;

@Component("fileFilter")
public class FileFilter extends GenericFilterBean {

//    private final static ConcurrentMap<String, FileItem> fileCache = new ConcurrentHashMap<String, FileItem>();

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    }

    private static final String regex = "_(\\d+)x(\\d+)[.]([^.]+)$";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String referer = WebUtil.getReferer(request);
        if (referer == null) {
            chain.doFilter(request, response);
            return;
        }

        //通过 referer 判断访问来源，并通过配置 文件管理器 与 host 关联。
        String host = RegexpUtil.parseFirst(referer, "(http://|https://)[^/]+");
        String url = request.getRequestURI().replaceAll("^" + request.getContextPath(), "");
        FileManager webrootFileManager = FileManagerFactory.getInstance().getFileManager("WEBROOT");
        if (RegexpUtil.find(url, ".do$")) {
            chain.doFilter(request, response);
            return;
        }

        if (FileManagerFactory.getInstance().getFileManager("WEBROOT").getFileItem(url) != null) {
            chain.doFilter(request, response);
            return;
        }
//        if (fileCache.containsKey(url)) {
//            writeFile(request, response, fileCache.get(url));
//            return;
//        }
        FileManager fileManager = SettingUtil.getDefaultUploadFileManager();
        if (fileManager != null) {
            FileItem fileItem = fileManager.getFileItem(url);
            if (fileItem == null && RegexpUtil.find(url, regex)) {
                // 查找源文件
                String srcUrl = RegexpUtil.replace(url, regex, ".$3");
//                if (fileCache.containsKey(srcUrl)) {
//                    fileItem = fileCache.get(srcUrl);
//                } else {
                    fileItem = fileManager.getFileItem(srcUrl);
                    if (fileItem == null) {
//						noInFileManagerCache.add(url);
                        chain.doFilter(request, response);
                        return;
                    }
                    // 缓存原始路径对应的文件
//                    if (!fileCache.containsKey(srcUrl)) {
//                        fileCache.put(srcUrl, fileItem);
//                    }
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
                webrootFileManager.writeFile(url, tmp);
                // 删除临时文件
                FileUtil.delFile(tmp);
//                fileCache.put(url, fileItem = webrootFileManager.getFileItem(url));
                writeFile(request, response, fileItem);
                return;
            }
//            if (fileItem != null) {
//                fileCache.put(url, fileItem);
//                writeFile(request, response, fileItem);
//                return;
//            }
//        }
        chain.doFilter(request, response);
    }

//    public static void addFileCache(String url, FileItem fileItem) {
//        fileCache.put(url, fileItem);
//    }
//
//    public static FileItem getFileCache(String url) {
//        return fileCache.get(url);
//    }


    public void _doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

      /*  System.out.println("==========================");*/

        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String o = enumeration.nextElement();
//            System.out.println("Header\t" + o + "=" + request.getHeader(o));
        }

        enumeration = request.getParameterNames();

        while (enumeration.hasMoreElements()) {
            String o = enumeration.nextElement();
           /* System.out.println("Parameter\t" + o + "=" + request.getHeader(o));*/
        }

      /*  System.out.println("Protocol:\t" + request.getProtocol());
        System.out.println("Method:\t" + request.getMethod());*/

        response.addHeader("Accept-Ranges", "bytes");
        response.addHeader("Cneonction", "close");

//        ServletUtils.setNoCacheHeader(response);

        String range = request.getHeader("Range");
        if ("keep-alive".equals(request.getHeader("connection")) && range != null) {
            File file = new File(PathUtil.root() + request.getRequestURI().replaceAll(request.getContextPath(), ""));

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
                end = (int) (file.length() - 1);
            } else if (bytes.endsWith("-")) {
                start = Integer.valueOf(sf[0]);
                end = (int) (file.length() - 1);
            }
            int contentLength = end - start + 1;

            response.setHeader("Connection", "keep-alive");
            response.setHeader("Content-Type", FileUtil.getMimeType(file));
            response.setHeader("Cache-Control", "max-age=1024");
            ServletUtils.setLastModifiedHeader(response, file.lastModified());
            response.setHeader("Content-Length", (contentLength > file.length() ? file.length() : contentLength) + "");
            response.setHeader("Content-Range", "bytes " + start + "-" + (end != 1 && end >= file.length() ? end - 1 : end) + "/" + file.length());

            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

          /*  System.out.println("start:" + start + "\tend:" + end + "\tcontentLength:" + contentLength);*/

            if (start > 0) {
                long s = in.skip(start);
              /*  System.out.println(start + "=" + s);*/
            }

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
            filterChain.doFilter(request, response);
        }

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
            try {
                StreamUtil.copy(fileItem.getInputStream(), response.getOutputStream());
            } catch (FileNotFoundException e) {
                response.sendError(404);
            }
        }
    }

}