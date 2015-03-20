package com.fantasy.framework.struts2.core.conversion;

import com.opensymphony.xwork2.FileManager;
import com.opensymphony.xwork2.FileManagerFactory;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.XWorkConstants;
import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ClassLoaderUtil;

import java.net.URL;
import java.util.Map;

public class FantasyXWorkConverter extends XWorkConverter {

    private FileManager fileManager;

    private boolean reloadingConfigs;

    @Inject
    public void setObjectFactory(ObjectFactory factory) {
        super.setObjectFactory(factory);
    }

    @Inject
    public void setDefaultTypeConverter(XWorkBasicConverter conv) {
        super.setDefaultTypeConverter(conv);
    }

    @Inject
    public void setFileManagerFactory(FileManagerFactory fileManagerFactory) {
        super.setFileManagerFactory(fileManagerFactory);
        this.fileManager = fileManagerFactory.getFileManager();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Object getConverter(Class clazz, String property) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Property: " + property);
            LOG.debug("Class: " + clazz.getName());
        }
        if ((property != null) && !noMapping.contains(clazz)) {
            try {
                Map<String, Object> mapping = mappings.get(clazz);

                if (mapping == null) {
                    mapping = buildConverterMapping(clazz);
                } else {
                    mapping = conditionalReload(clazz, mapping);
                }

                Object converter = mapping.get(property);
                if (LOG.isDebugEnabled() && converter == null) {
                    LOG.debug("converter is null for property " + property + ". Mapping size: " + mapping.size());
                    for (String next : mapping.keySet()) {
                        LOG.debug(next + ":" + mapping.get(next));
                    }
                }
                return converter;
            } catch (Exception t) {
                noMapping.add(clazz);
            }
        }
        return null;
    }

    @Inject(value = XWorkConstants.RELOAD_XML_CONFIGURATION, required = false)
    public void setReloadingConfigs(String reloadingConfigs) {
        super.setReloadingConfigs(reloadingConfigs);
        this.reloadingConfigs = Boolean.parseBoolean(reloadingConfigs);
    }

    @SuppressWarnings("rawtypes")
    private Map<String, Object> conditionalReload(Class clazz, Map<String, Object> oldValues) throws Exception {
        Map<String, Object> mapping = oldValues;

        if (this.reloadingConfigs) {
            URL fileUrl = ClassLoaderUtil.getResource(buildConverterFilename(clazz), clazz);
            if (fileUrl != null && fileManager.fileNeedsReloading(fileUrl.toString())) {
                mapping = buildConverterMapping(clazz);
            }
        }

        return mapping;
    }

}
