package com.fantasy.file.service;

import com.fantasy.common.service.FtpServiceFactory;
import com.fantasy.file.FileManager;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.bean.enums.FileManagerType;
import com.fantasy.file.manager.FTPFileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.file.manager.UploadFileManager;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.service.FTPService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    @Autowired
    private FtpServiceFactory ftpServiceFactory;

    private final static ConcurrentMap<String, FileManager> fileManagerCache = new ConcurrentHashMap<String, FileManager>();

    public void afterPropertiesSet() throws Exception {
    }

    public static String getFileManagerBeanId(String id) {
        return "FileManager_" + id;
    }

    public void initialize() {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 创建默认目录
            FileManagerConfig fileManagerConfig = fileManagerService.get(WEBROOT_FILEMANAGER_ID);
            if (fileManagerConfig == null) {
                fileManagerConfig = new FileManagerConfig();
                fileManagerConfig.setId(WEBROOT_FILEMANAGER_ID);
                fileManagerConfig.setName("项目WEB根目录");
                fileManagerConfig.setDescription("应用启动是检查并修改该目录");
                fileManagerConfig.setType(FileManagerType.local);
            }
            fileManagerConfig.setLocalDefaultDir(StringUtil.defaultValue(PathUtil.root(), PathUtil.classes()));
            fileManagerService.save(fileManagerConfig);
            if (logger.isDebugEnabled()) {
                StringBuffer log = new StringBuffer();
                log.append("\r\n初始化WEBROOT文件管理器(").append(WEBROOT_FILEMANAGER_ID).append("):");
                log.append("\r\n名称:").append(fileManagerConfig.getName());
                log.append("\r\n类型:").append(fileManagerConfig.getType().getValue());
                log.append("\r\n地址:").append(fileManagerConfig.getLocalDefaultDir());
                log.append("\r\n描述:").append(fileManagerConfig.getDescription());
                logger.debug(log);
            }
            // 初始化文件管理器
            for (FileManagerConfig config : fileManagerService.getAll()) {
                this.initialize(config);
            }
        } finally {
            transactionManager.commit(status);
        }
        // SpringContextUtil.registerBeanDefinition("xxxxx", ConfigFileManager.class);
        // System.out.println(SpringContextUtil.getBean("xxxxx"));
        // SpringContextUtil.registerBeanDefinition("xxxxx", DynamicFileManager.class);
        // System.out.println(SpringContextUtil.getBean("xxxxx"));
        // System.out.println(SpringContextUtil.getBean("xxxxx"));
        // System.out.println(Arrays.toString(SpringContextUtil.getBeanNamesForType(DynamicFileManager.class)));
    }

    /**
     * 获取 虚拟文件管理器
     *
     * @param config 配置信息
     * @return FileManager
     */
    private FileManager initialize(String beanId, FileManagerConfig config) {
        if (!fileManagerCache.containsKey(beanId)) {
            Map<String, Object> propertyValues = new HashMap<String, Object>();
            propertyValues.put("config", config);
            propertyValues.put("source", this.initialize(config.getSource()));
            SpringContextUtil.registerBeanDefinition(getFileManagerBeanId(config.getId()), UploadFileManager.class, propertyValues);
            fileManagerCache.put(config.getId(), SpringContextUtil.getBean(getFileManagerBeanId(config.getId()), FileManager.class));
        }
        return fileManagerCache.get(config.getId());
    }

    /**
     * 获取配置对应的 FileManager ，不包括虚拟目录文件管理器
     *
     * @param config 配置信息
     * @return FileManager
     */
    public FileManager initialize(FileManagerConfig config) {
        String beanId = config.getId();
        if (logger.isDebugEnabled()) {
            StringBuffer log = new StringBuffer();
            log.append("\r\n初始化文件管理器(").append(beanId).append("):");
            log.append("\r\n名称:").append(config.getName());
            log.append("\r\n类型:").append(config.getType().getValue());
            log.append("\r\n地址:").append(config.getLocalDefaultDir());
            log.append("\r\n描述:").append(config.getDescription());
            logger.debug(log);
        }
        try {
            if (FileManagerType.virtual == config.getType()) {
                return initialize(beanId, config);
            } else if (FileManagerType.local == config.getType()) {// 本地文件管理
                return initialize(beanId, config.getLocalDefaultDir());
            } else if (FileManagerType.ftp == config.getType()) {// FTP文件管理
                return initialize(beanId, ftpServiceFactory.getFtpService(config.getFtpConfig().getId()));
            } else if (FileManagerType.jdbc == config.getType()) {// JDBC文件管理
                return null;
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        throw new IgnoreException(config.getType() + " 对应的FileManagerType不存在!");
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
        if (fileManagerCache.isEmpty()) {
            this.initialize();
        }
        return fileManagerCache.containsKey(id) ? fileManagerCache.get(id) : null;
    }

    /**
     * 获取ftpService对应的文件管理器
     *
     * @param ftpService FTP service
     * @return FileManager
     */
    public FileManager initialize(String beanId, FTPService ftpService) {
        FTPFileManager fileManager = (FTPFileManager) fileManagerCache.get(beanId);
        if (!fileManagerCache.containsKey(beanId)) {
            SpringContextUtil.registerBeanDefinition(getFileManagerBeanId(beanId), FTPFileManager.class, new Object[]{ftpService});
            fileManagerCache.put(beanId, SpringContextUtil.getBean(getFileManagerBeanId(beanId), FileManager.class));
        } else {
            fileManager.setFtpService(ftpService);
        }
        return fileManagerCache.get(beanId);
    }

    /**
     * 获取本地文件管理器
     *
     * @param beanId     id
     * @param defaultDir 路径
     * @return FileManager
     */
    public FileManager initialize(String beanId, String defaultDir) {
        LocalFileManager fileManager = (LocalFileManager) fileManagerCache.get(beanId);
        if (!fileManagerCache.containsKey(beanId)) {
            Map<String, Object> propertyValues = new HashMap<String, Object>();
            propertyValues.put("defaultDir", defaultDir);
            SpringContextUtil.registerBeanDefinition(getFileManagerBeanId(beanId), LocalFileManager.class, propertyValues);
            fileManagerCache.put(beanId, fileManager = (LocalFileManager) SpringContextUtil.getBean(getFileManagerBeanId(beanId), FileManager.class));
        } else {
            fileManager.setDefaultDir(defaultDir);
        }
        return fileManager;
    }

    public static String[] getFileManagerIds() {
        if (fileManagerCache.isEmpty()) {
            getInstance().initialize();
        }
        Set<String> strings = fileManagerCache.keySet();
        return strings.toArray(new String[strings.size()]);
    }

    public void remove(FileManagerConfig config) {
        fileManagerCache.remove(config.getId());
        SpringContextUtil.removeBeanDefinition(getFileManagerBeanId(config.getId()));
    }

    public static List<FileManagerConfig> getFileManagers() {
        return SpringContextUtil.getBeanByType(FileManagerService.class).listFileManager();
    }

    public static String getConfigId(FileManager fm) {
        for (Map.Entry<String, FileManager> entry : fileManagerCache.entrySet()) {
            if (entry.getValue().equals(fm)) {
                return entry.getKey();
            }
        }
        throw new IgnoreException(fm + "-对应的文件管理器未注册!");
    }

}
