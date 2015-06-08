package com.fantasy.framework.struts2.core.validator;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ClassLoaderUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.validator.*;
import com.opensymphony.xwork2.validator.validators.VisitorFieldValidator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 扩展 AnnotationActionValidatorManager 的验证<br/>
 * 使其支持数组及List的验证<br/>
 *
 * @author lmf
 */
@SuppressWarnings("unchecked")
public class FantasyAnnotationActionValidatorManager extends com.opensymphony.xwork2.validator.AnnotationActionValidatorManager {

    private static final Logger LOG = LoggerFactory.getLogger(FantasyAnnotationActionValidatorManager.class);

    private static Method getFieldValueMethod = null;
    private ValidatorFactory validatorFactory;

    static {
        try {
            getFieldValueMethod = Class.forName("com.opensymphony.xwork2.validator.validators.ValidatorSupport").getDeclaredMethod("getFieldValue", String.class, Object.class);
            getFieldValueMethod.setAccessible(true);
        } catch (Exception e) {
            LOG.error("load getFieldValueMethod {0}", e);
        }
    }

    @Inject
    public void setValidatorFactory(ValidatorFactory fac) {
        if (validatorFactory != null) {
            return;
        }
        super.setValidatorFactory(validatorFactory = fac);

        Set<String> sets = new HashSet<String>();
        try {
            Iterator<URL> urls = ClassLoaderUtil.getResources("", DefaultValidatorFactory.class, false);
            while (urls.hasNext()) {
                URL u = urls.next();
                sets.add(u.getPath());
            }
        } catch (IOException e) {
            throw new ConfigurationException("Unable to parse validators", e);
        }

        List<File> files = new ArrayList<File>();

        try {
            Iterator<URL> urls = ClassLoaderUtil.getResources("struts-plugin.xml", DefaultValidatorFactory.class, false);
            while (urls.hasNext()) {
                URL u = urls.next();
                try {
                    String jarPath = u.getPath().replaceAll(" ", "%20").replaceFirst("!/struts-plugin.xml$", "");
                    URI uri = new URI(u.toExternalForm().replaceAll(" ", "%20"));
                    if (!sets.contains(jarPath) && uri.isOpaque() && "jar".equalsIgnoreCase(uri.getScheme())) {
                        ZipInputStream zipInputStream = null;
                        try {
                            InputStream inputStream = new URL(jarPath).openStream();
                            if (inputStream instanceof ZipInputStream) {
                                zipInputStream = (ZipInputStream) inputStream;
                            } else {
                                zipInputStream = new ZipInputStream(inputStream);
                            }
                            ZipEntry zipEntry = zipInputStream.getNextEntry();
                            while (zipEntry != null) {
                                if (zipEntry.getName().endsWith("-validators.xml")) {
                                    if (LOG.isTraceEnabled()) {
                                        LOG.trace("Adding validator " + zipEntry.getName());
                                    }
                                    files.add(new File(zipEntry.getName()));
                                }
                                zipEntry = zipInputStream.getNextEntry();
                            }
                        } finally {
                            if (zipInputStream != null) {
                                zipInputStream.close();
                            }
                        }

                    }
                } catch (Exception ex) {
                    LOG.error("Unable to load #0", ex, u.toString());
                }
            }
        } catch (IOException e) {
            throw new ConfigurationException("Unable to parse validators", e);
        }
        // Add custom (plugin) specific validator configurations
        for (File file : files) {
            retrieveValidatorConfiguration(file.getName());
        }
    }

    private void retrieveValidatorConfiguration(String resourceName) {
        InputStream is = ClassLoaderUtil.getResourceAsStream(resourceName, DefaultValidatorFactory.class);
        if (is != null) {
            ValidatorFileParser validatorFileParser = (ValidatorFileParser) ClassUtil.getValue(this.validatorFactory, "validatorFileParser");
            Map<String, String> validators = (Map<String, String>) ClassUtil.getValue(this.validatorFactory, "validators");
            validatorFileParser.parseValidatorDefinitions(validators, is, resourceName);
        }
    }

    @SuppressWarnings("rawtypes")
    public void validate(Object object, String context, ValidatorContext validatorContext, String method) throws ValidationException {
        List<Validator> validators = getValidators(object.getClass(), context, method);
        Set<String> shortcircuitedFields = null;
        for (final Validator validator : validators) {
            try {
                validator.setValidatorContext(validatorContext);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Running validator: " + validator + " for object " + object + " and method " + method);
                }
                FieldValidator fValidator = null;
                String fullFieldName = null;
                if (validator instanceof FieldValidator) {
                    fValidator = (FieldValidator) validator;
                    fullFieldName = new InternalValidatorContextWrapper(fValidator.getValidatorContext()).getFullFieldName(fValidator.getFieldName());
                    if ((shortcircuitedFields != null) && shortcircuitedFields.contains(fullFieldName)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Short-circuited, skipping");
                        }
                        continue;
                    }
                }
                if (validator instanceof ShortCircuitableValidator && ((ShortCircuitableValidator) validator).isShortCircuit()) {
                    // get number of existing errors
                    List<String> errs = null;
                    if (fValidator != null) {
                        if (validatorContext.hasFieldErrors()) {
                            Collection<String> fieldErrors = validatorContext.getFieldErrors().get(fullFieldName);
                            if (fieldErrors != null) {
                                errs = new ArrayList<String>(fieldErrors);
                            }
                        }
                    } else if (validatorContext.hasActionErrors()) {
                        Collection<String> actionErrors = validatorContext.getActionErrors();
                        if (actionErrors != null) {
                            errs = new ArrayList<String>(actionErrors);
                        }
                    }
                    validator.validate(object);
                    if (fValidator != null) {
                        if (validatorContext.hasFieldErrors()) {
                            Collection<String> errCol = validatorContext.getFieldErrors().get(fullFieldName);
                            if ((errCol != null) && !errCol.equals(errs)) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Short-circuiting on field validation");
                                }
                                if (shortcircuitedFields == null) {
                                    shortcircuitedFields = new TreeSet<String>();
                                }
                                shortcircuitedFields.add(fullFieldName);
                            }
                        }
                    } else if (validatorContext.hasActionErrors()) {
                        Collection<String> errCol = validatorContext.getActionErrors();
                        if ((errCol != null) && !errCol.equals(errs)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Short-circuiting");
                            }
                            break;
                        }
                    }
                    continue;
                }
                if (fValidator != null) {
                    if (fullFieldName.indexOf("[*]") > 0) {
                        validate(object, fullFieldName, fValidator);
                    } else {
                        validator.validate(object);
                    }
                } else {
                    validator.validate(object);
                }
            } finally {
                validator.setValidatorContext(null);
            }

        }
    }

    @SuppressWarnings("rawtypes")
    public void validate(Object object, String fullFieldName, FieldValidator fValidator) throws ValidationException {
        try {
            String beanName = RegexpUtil.parseGroup(fullFieldName, "([a-zA-Z0-9_\\[\\].]+)\\[\\*\\]", 1);
            String fieldName = fullFieldName.replace(beanName + "[*]", "");
            Object val = getFieldValueMethod.invoke(fValidator, beanName, object);
            if (val instanceof List) {
                val = ((List) val).toArray(new Object[((List) val).size()]);
            }
            if (val.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(val); i++) {
                    String fn = beanName + "[" + i + "]" + fieldName;
                    if (fn.indexOf("[*]") > 0) {
                        validate(object, fn, fValidator);
                    } else {
                        fValidator.setFieldName(fn);
                        fValidator.validate(object);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("getFieldValueMethod invoke errorMsg={0}", e.getMessage());
        }
    }

    protected static class InternalValidatorContextWrapper {
        private ValidatorContext validatorContext = null;

        InternalValidatorContextWrapper(ValidatorContext validatorContext) {
            this.validatorContext = validatorContext;
        }

        public String getFullFieldName(String field) {
            if (validatorContext instanceof VisitorFieldValidator.AppendingValidatorContext) {
                VisitorFieldValidator.AppendingValidatorContext appendingValidatorContext = (VisitorFieldValidator.AppendingValidatorContext) validatorContext;
                return appendingValidatorContext.getFullFieldNameFromParent(field);
            }
            return validatorContext.getFullFieldName(field);
        }
    }
}