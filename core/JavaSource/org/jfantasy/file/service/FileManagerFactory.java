package org.jfantasy.file.service;

import org.jfantasy.file.FileManager;
import org.jfantasy.file.FileManagerBuilder;
import org.jfantasy.file.bean.ConfigParam;
import org.jfantasy.file.bean.FileManagerConfig;
import org.jfantasy.file.bean.enums.FileManagerType;
import org.jfantasy.file.builders.LocalFileManagerBuilder;
import org.jfantasy.file.manager.UploadFileManager;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.JdbcUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.jfantasy.file.bean.enums.FileManagerType.local;

/**
 * FileManager 管理类
 * 1.从数据库。初始化FileManager类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午03:57:31
 */
@Component
@Transactional
public class FileManagerFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static final Log LOG = LogFactory.getLog(FileManagerFactory.class);

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

    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(fileManagerCache.isEmpty()) {
            this.load();
        }
    }

    public void load() {
        long start = System.currentTimeMillis();
        JdbcUtil.transaction(new JdbcUtil.Callback<Void>() {
            @Override
            public Void run() {
                // 创建默认目录
                final String webrootPath = StringUtil.defaultValue(PathUtil.root(), PathUtil.classes());
                FileManagerConfig fileManagerConfig = fileManagerService.get(WEBROOT_FILEMANAGER_ID);
                if (fileManagerConfig == null) {
                    fileManagerService.save(local, WEBROOT_FILEMANAGER_ID, "项目WEB根目录", "应用启动是检查并修改该目录", new HashMap<String, String>() {
                        {
                            this.put("defaultDir", webrootPath);
                        }
                    });
                } else {
                    ConfigParam defaultDir = ObjectUtil.find(fileManagerConfig.getConfigParams(), "name", "defaultDir");
                    if (defaultDir == null || !webrootPath.equals(defaultDir.getValue())) {
                        fileManagerConfig.addConfigParam("defaultDir", webrootPath);
                        fileManagerService.save(fileManagerConfig);
                    }
                }
                // 初始化文件管理器
                for (FileManagerConfig config : fileManagerService.getAll()) {
                    try {
                        FileManagerFactory.this.registerFileManager(config.getId(), config.getType(), config.getConfigParams());
                    } catch (Exception ex) {
                        LOG.error("注册 FileManager id = [" + config.getId() + "] 失败!", ex);
                    }
                }
                return null;
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED);
        LOG.error("\n初始化 FileManagerFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    public void registerFileManager(String id, FileManagerType type, final List<ConfigParam> configParams) {
        if (!fileManagerBuilders.containsKey(type)) {
            LOG.error(" 未找到 [" + type + "] 对应的构建程序!请参考 FileManagerBuilder 实现,并添加到 FileManagerFactory 的配置中");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        for (ConfigParam configParam : configParams) {
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
        if(fileManagerCache.isEmpty()) {
            this.load();
        }
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
