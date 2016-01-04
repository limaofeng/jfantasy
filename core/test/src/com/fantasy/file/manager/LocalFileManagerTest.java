package org.jfantasy.file.manager;

import org.jfantasy.file.FileItem;
import org.jfantasy.file.FileManager;
import org.jfantasy.framework.util.common.PathUtil;
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