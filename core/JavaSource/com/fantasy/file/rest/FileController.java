package com.fantasy.file.rest;


import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FilePartService;
import com.fantasy.file.service.FileService;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private transient FileService fileService;
    @Autowired
    private transient FileUploadService fileUploadService;
    @Autowired
    private transient FilePartService filePartService;

    /**
     * @param file              要上传的文件
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
    @RequestMapping(method = RequestMethod.POST)
    public FileDetail upload(@RequestParam(value = "attach", required = false) MultipartFile file,String dir, String entireFileName, String entireFileDir, String entireFileHash, String partFileHash, Integer total, Integer index) throws IOException {
        FileDetail fileDetail = fileUploadService.upload(file, dir, entireFileName, entireFileDir, entireFileHash, partFileHash, total, index);
        if (WebUtil.browser(ActionContext.getContext().getHttpRequest()) == WebUtil.Browser.msie) {
            PrintWriter writer = ActionContext.getContext().getHttpResponse().getWriter();
            writer.print(JSON.serialize(fileDetail));
            writer.flush();
        }
        return fileDetail;
    }

}
