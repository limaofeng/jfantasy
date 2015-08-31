package com.fantasy.file.rest;


import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.file.bean.FilePart;
import com.fantasy.file.service.FilePartService;
import com.fantasy.file.service.FileService;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param file           要上传的文件
     * @param dir            上传的目录标识
     * @param entireFileName 完整文件名
     * @param entireFileDir  完整文件的上传目录标识
     * @param entireFileHash 完整文件Hash值
     * @param partFileHash   分段文件Hash值
     * @param total          总段数
     * @param index          分段序号
     * @return {String} 返回文件信息
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public FileDetail upload(@RequestParam(value = "attach", required = false) MultipartFile file, String dir, String entireFileName, String entireFileDir, String entireFileHash, String partFileHash, Integer total, Integer index) throws IOException {
        FileDetail fileDetail = fileUploadService.upload(file, dir, entireFileName, entireFileDir, entireFileHash, partFileHash, total, index);
        if (WebUtil.browser(ActionContext.getContext().getHttpRequest()) == WebUtil.Browser.msie) {
            PrintWriter writer = ActionContext.getContext().getHttpResponse().getWriter();
            writer.print(JSON.serialize(fileDetail));
            writer.flush();
        }
        return fileDetail;
    }

    @RequestMapping(value = "/{hash}/pass", method = RequestMethod.GET)
    public Map<String, Object> pass(@PathVariable("hash") String hash) {
        List<FilePart> parts = filePartService.find(hash);
        Map<String, Object> data = new HashMap<String, Object>();
        FilePart part = ObjectUtil.remove(parts, "index", 0);
        if (part != null) {
            data.put("fileDetail", fileService.get(FileDetailKey.newInstance(part.getAbsolutePath(), part.getFileManagerId())));
        }
        data.put("parts", parts);
        return data;
    }


}
