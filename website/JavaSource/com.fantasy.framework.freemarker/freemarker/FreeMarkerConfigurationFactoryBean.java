package org.jfantasy.framework.freemarker;

import org.jfantasy.framework.freemarker.directive.DirectiveUtils;
import org.jfantasy.framework.util.common.ClassUtil;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展 spring FreeMarkerConfigurationFactory 类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-12-3 下午02:13:53
 */
@SuppressWarnings("rawtypes")
public class FreeMarkerConfigurationFactoryBean extends FreeMarkerConfigurationFactory implements FactoryBean, InitializingBean, ResourceLoaderAware, ApplicationContextAware {

    private Map<String, Object> utils = new HashMap<String, Object>();
    private Map<String, TemplateModel> tags = new HashMap<String, TemplateModel>();
    private WebappTemplateLoader webappTemplateLoader;

    private Configuration configuration;

    public void afterPropertiesSet() throws IOException, TemplateException {
        this.configuration = createConfiguration();
        DirectiveUtils.exposeRapidMacros(this.configuration);
        for (Map.Entry<String, TemplateModel> entry : tags.entrySet()) {
            configuration.setSharedVariable(entry.getKey(), entry.getValue());
        }
        if (!utils.containsKey("DateUtil")) {
            utils.put("DateUtil", "org.jfantasy.framework.util.common.DateUtil");
        }

        if (!utils.containsKey("ClassUtil")) {
            utils.put("ClassUtil", "org.jfantasy.framework.util.common.ClassUtil");
        }

        if (!utils.containsKey("StringUtil")) {
            utils.put("StringUtil", "org.jfantasy.framework.util.common.StringUtil");
        }

        if (!utils.containsKey("RegexpUtil")) {
            utils.put("RegexpUtil", "org.jfantasy.framework.util.regexp.RegexpUtil");
        }

        if (!utils.containsKey("NumberUtil")) {
            utils.put("NumberUtil", "org.jfantasy.framework.util.common.NumberUtil");
        }

        if (!utils.containsKey("JSON")) {
            utils.put("JSON", "org.jfantasy.framework.util.jackson.JSON");
        }

        if (!utils.containsKey("HtmlUtils")) {
            utils.put("HtmlUtils", "org.springframework.web.util.HtmlUtils");
        }

        for (Map.Entry<String, Object> entry : utils.entrySet()) {
            if (entry.getValue() instanceof String) {
                configuration.setSharedVariable(entry.getKey(), useStaticPackage(entry.getValue().toString()));
            }
        }
    }

    @Override
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        Class<TemplateLoader> strutsClassTemplateLoader = ClassUtil.forName("org.apache.struts2.views.freemarker.StrutsClassTemplateLoader");
        if (strutsClassTemplateLoader != null) {
            templateLoaders.add(ClassUtil.newInstance(strutsClassTemplateLoader));
        }
        if (webappTemplateLoader != null) {
            templateLoaders.add(webappTemplateLoader);
        }
    }

    public Configuration getConfiguration() {
        return getObject();
    }

    public Configuration getObject() {
        return this.configuration;
    }

    public Class<Configuration> getObjectType() {
        return Configuration.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void addUtil(String utilName, Object util) {
        this.utils.put(utilName, util);
    }

    public void setUtils(Map<String, Object> utils) {
        this.utils = utils;
    }

    /**
     * 生成Freemarker 静态方法类
     *
     * @param packageName 为类的全路径
     * @return TemplateModel
     */
    protected TemplateModel useStaticPackage(String packageName) {
        try {
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            return staticModels.get(packageName);
        } catch (TemplateModelException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public void setTags(Map<String, TemplateModel> tags) {
        this.tags = tags;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext) {
            webappTemplateLoader = new WebappTemplateLoader(((WebApplicationContext) applicationContext).getServletContext());
        }
    }

}
