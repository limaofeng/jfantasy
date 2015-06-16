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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
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
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public FileDetail upload(@RequestParam(value = "attach", required = false) MultipartFile file, String dir, String entireFileName, String entireFileDir, String entireFileHash, String partFileHash, Integer total, Integer index) throws IOException {
        FileDetail fileDetail = fileUploadService.upload(file, dir, entireFileName, entireFileDir, entireFileHash, partFileHash, total, index);
        if (WebUtil.browser(ActionContext.getContext().getHttpRequest()) == WebUtil.Browser.msie) {
            PrintWriter writer = ActionContext.getContext().getHttpResponse().getWriter();
            writer.print(JSON.serialize(fileDetail));
            writer.flush();
        }
        return fileDetail;
    }

    @RequestMapping(value = "/upload/pass", method = RequestMethod.GET)
    public Map<String, Object> pass(String hash) {
        List<FilePart> parts = filePartService.find(hash);
        Map<String, Object> data = new HashMap<String, Object>();
        FilePart part = ObjectUtil.remove(parts, "index", 0);
        if (part != null) {
            data.put("fileDetail", fileService.get(FileDetailKey.newInstance(part.getAbsolutePath(), part.getFileManagerId())));
        }
        data.put("parts", parts);
        return data;
    }

    //TODO 添加图片切图处理程序，或许HTML5可以直接支持
//    public String photoShot(String picture,String toPicture,int y,int x,int w,int h,float ratio) throws Exception {
//        ImageUtil.write(ImageUtil.screenshots(PathUtil.getFilePath(picture), x, y, w, h, ratio), PathUtil.getFilePath(toPicture));
//        return "success";
//    }

}