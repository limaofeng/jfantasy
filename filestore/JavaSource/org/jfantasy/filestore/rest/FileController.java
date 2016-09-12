package org.jfantasy.filestore.rest;


import org.jfantasy.filestore.bean.FileDetail;
import org.jfantasy.filestore.bean.FileDetailKey;
import org.jfantasy.filestore.bean.FilePart;
import org.jfantasy.filestore.service.FilePartService;
import org.jfantasy.filestore.service.FileService;
import org.jfantasy.filestore.service.FileUploadService;
import org.jfantasy.framework.util.common.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "files", description = "文件上传接口")
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
     * @throws IOException 文件上传异常
     */
    @ApiOperation(value = "上传文件", notes = "单独的文件上传接口，返回 FileDetail 对象", response = FileDetail.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public FileDetail upload(@ApiParam(value = "上传的附件", required = true) @RequestParam(value = "attach") MultipartFile file, @ApiParam(value = "上传目录编码", required = true) String dir, @ApiParam(value = "完整文件名") String entireFileName, @ApiParam(value = "完整文件上传目录") String entireFileDir, @ApiParam(value = "文章文件 Hash 值") String entireFileHash, @ApiParam(value = "片段文件 Hash 值") String partFileHash, @ApiParam(value = "片段文件总段数据") Integer total, @ApiParam(value = "当前片段序号") Integer index) throws IOException {
        return fileUploadService.upload(file, dir, entireFileName, entireFileDir, entireFileHash, partFileHash, total, index);
    }

    @ApiOperation(value = "分段上传查询", notes = "根据文件 hash 值，判断文件上传进度")
    @RequestMapping(value = "/{hash}/pass", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> pass(@ApiParam(value = "文件 hash 值", required = true) @PathVariable("hash") String hash) {
        List<FilePart> parts = filePartService.find(hash);
        Map<String, Object> data = new HashMap<String, Object>();
        FilePart part = ObjectUtil.remove(parts, "index", 0);
        if (part != null) {
            data.put("fileDetail", fileService.get(FileDetailKey.newInstance(part.getAbsolutePath(), part.getFileManagerId())));
        }
        data.put("parts", parts);
        return data;
    }

    @ApiOperation("查询文件信息")
    @RequestMapping(value = "/{mid}:{path}", method = RequestMethod.GET)
    @ResponseBody
    public FileDetail view(HttpServletRequest request,@PathVariable("mid") String mid, @PathVariable("path") String path) {
        return fileService.get(FileDetailKey.newInstance(path.replaceAll("-", "/").concat(".").concat(WebUtil.getExtension(request)), mid));
    }

}
