package com.fantasy.file.service;

import com.fantasy.file.FileManager;
import com.fantasy.file.FileManagerBuilder;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.bean.FileManagerConfig.ConfigParam;
import com.fantasy.file.bean.enums.FileManagerType;
import com.fantasy.file.builders.LocalFileManagerBuilder;
import com.fantasy.file.manager.UploadFileManager;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.fantasy.file.bean.enums.FileManagerType.local;

/**
 * FileManager 管理类
 * 1.从数据库。初始化FileManager类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午03:57:31
 */
@Transactional
public class FileManagerFactory implements InitializingBean {

    private static final Log logger = LogFactory.getLog(FileManagerFactory.class);

    private static FileManagerFactory fileManagerFactory;

    public final static String WEBROOT_FILEMANAGER_ID = "WEBROOT";

    @Autowired
    private FileManagerService fileManagerService;

    private Map<FileManagerType, FileManagerBuilder> fileManagerBuilders = new HashMap<FileManagerType, FileManagerBuilder>() {
        {
            this.put(FileManagerType.local, new LocalFileManagerBuilder());
        }
    };

    private final static ConcurrentMap<String, FileManager> fileManagerCache = new ConcurrentHashMap<String, FileManager>();


    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        assert transactionManager != null;
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 创建默认目录
            final String webroot_path = StringUtil.defaultValue(PathUtil.root(), PathUtil.classes());
            FileManagerConfig fileManagerConfig = fileManagerService.get(WEBROOT_FILEMANAGER_ID);
            if (fileManagerConfig == null) {
                fileManagerService.save(local, WEBROOT_FILEMANAGER_ID, "项目WEB根目录", "应用启动是检查并修改该目录", new HashMap<String, String>() {
                    {
                        this.put("defaultDir", webroot_path);
                    }
                });
            } else {
                ConfigParam defaultDir = ObjectUtil.find(fileManagerConfig.getConfigParams(), "name", "defaultDir");
                if (defaultDir == null || !webroot_path.equals(defaultDir.getValue())) {
                    fileManagerConfig.addConfigParam("defaultDir", webroot_path);
                    fileManagerService.save(fileManagerConfig);
                }
            }
            // 初始化文件管理器
            for (FileManagerConfig config : fileManagerService.getAll()) {
                try {
                    this.registerFileManager(config.getId(), config.getType(), config.getConfigParams());
                } catch (Exception ex) {
                    logger.error("注册 FileManager id = [" + config.getId() + "] 失败!", ex);
                }
            }
        } finally {
            transactionManager.commit(status);
        }
    }

    public void registerFileManager(String id, FileManagerType type, final List<FileManagerConfig.ConfigParam> configParams) {
        if (!fileManagerBuilders.containsKey(type)) {
            logger.error(" 未找到 [" + type + "] 对应的构建程序!请参考 FileManagerBuilder 实现,并添加到 FileManagerFactory 的配置中");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        for (FileManagerConfig.ConfigParam configParam : configParams) {
            params.put(configParam.getName(), configParam.getValue());
        }
        fileManagerCache.put(id, fileManagerBuilders.get(type).register(params));
    }

    /**
     * 获取文件管理的单例对象
     *
     * @return FileManagerFactory
     */
    public synchronized static FileManagerFactory getInstance() {
        if (fileManagerFactory == null) {
            fileManagerFactory = SpringContextUtil.getBeanByType(FileManagerFactory.class);
        }
        return fileManagerFactory;
    }

    /**
     * 获取webroot对应的文件管理器
     *
     * @return FileManager
     */
    public static FileManager getWebRootFileManager() {
        return FileManagerFactory.getInstance().getFileManager(WEBROOT_FILEMANAGER_ID);
    }

    /**
     * 根据id获取对应的文件管理器
     *
     * @param id Id
     * @return UploadFileManager
     */
    public UploadFileManager getUploadFileManager(String id) {
        FileManager fileManager = getFileManager(id);
        if (fileManager == null) {
            throw new IgnoreException("文件管理器[" + id + "]不存在!");
        }
        if (!(fileManager instanceof UploadFileManager)) {
            throw new IgnoreException("文件管理器[" + id + "]不支持文件上传功能!");
        }
        return (UploadFileManager) fileManager;
    }

    /**
     * 根据id获取对应的文件管理器
     *
     * @param id Id
     * @return FileManager
     */
    public FileManager getFileManager(String id) {
        return fileManagerCache.get(id);
    }

    public static String[] getFileManagerIds() {
        Set<String> strings = fileManagerCache.keySet();
        return strings.toArray(new String[strings.size()]);
    }

    public void remove(FileManagerConfig config) {
        fileManagerCache.remove(config.getId());
    }

    public void setBuilders(List<FileManagerBuilder> builders) {
        for (FileManagerBuilder fileManagerBuilder : builders) {
            this.fileManagerBuilders.put(fileManagerBuilder.getType(), fileManagerBuilder);
        }
    }
}
