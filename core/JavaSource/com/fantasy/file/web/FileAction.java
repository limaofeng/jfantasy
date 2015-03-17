package com.fantasy.file.web;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileManager;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.file.bean.FilePart;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.file.service.FilePartService;
import com.fantasy.file.service.FileService;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.WebUtil.Browser;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileAction extends ActionSupport {

    private static final long serialVersionUID = -8084517912796997973L;

    @Autowired
    private transient FileService fileService;
    @Autowired
    private transient FileUploadService fileUploadService;
    @Autowired
    private transient FilePartService filePartService;

    /**
     * @param attach            要上传的文件
     * @param attachContentType contentType
     * @param attachFileName    名称
     * @param dir               上传的目录标识
     * @param entireFileName    完整文件名
     * @param entireFileDir     完整文件的上传目录标识
     * @param entireFileHash    完整文件Hash值
     * @param partFileHash      分段文件Hash值
     * @param total             总段数
     * @param index             分段序号
     * @return {String} 返回文件信息
     * @throws IOException
     */
    public String upload(File attach, String attachContentType, String attachFileName, String dir, String entireFileName, String entireFileDir, String entireFileHash, String partFileHash, Integer total, Integer index) throws IOException {
        FileDetail fileDetail = fileUploadService.upload(attach, attachContentType, attachFileName, dir, entireFileName, entireFileDir, entireFileHash, partFileHash, total, index);
        if (WebUtil.browser(ServletActionContext.getRequest()) == WebUtil.Browser.msie) {
            ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
            PrintWriter writer = ServletActionContext.getResponse().getWriter();
            writer.print(JSON.serialize(fileDetail));
            writer.flush();
            return NONE;
        }
        this.attrs.put(ROOT, fileDetail);
        return JSONDATA;
    }

    public String pass(String hash) {
        List<FilePart> parts = filePartService.find(hash);
        Map<String, Object> data = new HashMap<String, Object>();
        FilePart part = ObjectUtil.remove(parts, "index", 0);
        if (part != null) {
            data.put("fileDetail", fileService.get(FileDetailKey.newInstance(part.getAbsolutePath(), part.getFileManagerId())));
        }
        data.put("parts", parts);
        this.attrs.put(ROOT, data);
        return JSONDATA;
    }

    /**
     * 文件下载
     *
     * @throws IOException
     */
    public void download(String filePath) throws IOException {
        ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
        FileDetail fileDetail = fileService.getFileDetail(filePath, "haolue-upload");
        this.response.setContentType(fileDetail.getContentType());
        this.response.setCharacterEncoding("UTF-8");
        this.response.setHeader("Expires", "0");
        this.response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        this.response.setHeader("Pragma", "public");
        this.response.setContentLength(fileDetail.getSize().intValue());

        String fileName = (Browser.mozilla == WebUtil.browser(request) ? new String(fileDetail.getFileName().getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(fileDetail.getFileName(), "UTF-8"));
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        FileManagerFactory.getInstance().getFileManager(fileDetail.getFileManagerId()).readFile(filePath, response.getOutputStream());
    }

    public String update(FileDetail detail) {
        this.attrs.put(ROOT, fileService.update(detail));
        return JSONDATA;
    }

    /**
     * 文件查看页
     *
     * @param fileManagerId 文件管理器
     * @param path          查看的目录
     * @return {String}
     */
    public String files(String fileManagerId, String path) {
        FileManager fileManager = FileManagerFactory.getInstance().getFileManager(fileManagerId);
        path = StringUtil.defaultValue(path, "/");
        this.attrs.put("files", fileManager.listFiles(path));
        List<FileItem> nodes = new ArrayList<FileItem>();
        if (!"/".equals(path)) {
            FileItem fileItem = fileManager.getFileItem(path);
            nodes.add(fileItem);
            while (fileItem.getParentFileItem() != null && !"/".equals(fileItem.getParentFileItem().getAbsolutePath())) {
                fileItem = fileItem.getParentFileItem();
                nodes.add(0, fileItem);
            }
        }
        this.attrs.put("nodes", nodes);
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 用于fileManager加载文件
     *
     * @param fileManagerId 文件管理器
     * @param path          查看的目录
     * @return {String}
     */
    public String listFiles(String fileManagerId, String path) {
        this.attrs.put(ROOT, FileManagerFactory.getInstance().getFileManager(fileManagerId).listFiles(path));
        return JSONDATA;
    }

}
