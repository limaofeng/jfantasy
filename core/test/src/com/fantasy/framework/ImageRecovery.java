package org.jfantasy.framework;

import org.jfantasy.file.FileItem;
import org.jfantasy.file.FileItemSelector;
import org.jfantasy.file.FileManager;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.manager.LocalFileManager;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.util.common.MessageDigestUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ImageRecovery {

    private final static Log logger = LogFactory.getLog(ImageRecovery.class);

    @Autowired
    private FileService fileService;

    @Test
    public void recovery() throws IOException {
        FileManager fileManager = new LocalFileManager("/Users/lmf/Downloads/rex网站图片/");
        final FileManager rexFiles = new LocalFileManager("/Users/lmf/Downloads/rex网站图片-files");
        fileManager.listFiles(new FileItemSelector() {
            @Override
            public boolean includeFile(FileItem fileItem) {
                if (!fileItem.isDirectory()) {
                    try {
                        File file = new File("/Users/lmf/Downloads/rex网站图片" + fileItem.getAbsolutePath());
                        String md5 = MessageDigestUtil.getInstance().get(file);
                        FileDetail fileDetail = fileService.findUniqueByMd5(md5, "haolue-upload");
                        if (fileDetail != null) {
                            logger.error("writeFile : " + fileDetail.getRealPath());
                            rexFiles.writeFile(fileDetail.getRealPath(), file);
                        } else {
                            logger.error(fileItem.getAbsolutePath() + "=>" + md5);
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
                return true;
            }

            @Override
            public boolean traverseDescendents(FileItem fileItem) {
                System.out.println(fileItem.getAbsolutePath());
                return true;
            }
        });
    }

}
