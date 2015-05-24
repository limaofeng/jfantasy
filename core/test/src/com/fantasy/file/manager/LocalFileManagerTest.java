package com.fantasy.file.manager;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileManager;
import com.fantasy.framework.util.common.PathUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class LocalFileManagerTest {

    private FileManager fileManager = new LocalFileManager(PathUtil.classes());

    private static final Log LOG = LogFactory.getLog(LocalFileManagerTest.class);

    @Test
    public void testWriteFile() throws Exception {

    }

    @Test
    public void testReadFile() throws Exception {

    }

    @Test
    public void testGetFileItem() throws Exception {
        FileItem fileItem = fileManager.getFileItem("/log4j.xml");
        Assert.assertNotNull(fileItem);
    }

    @Test
    public void testListFiles() throws Exception {
        for(FileItem fileItem : fileManager.listFiles()){
            LOG.debug(fileItem.getAbsolutePath());
        }
    }


}